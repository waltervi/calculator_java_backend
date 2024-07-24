package com.company.backendcalculator.operations.service.implementation;

import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.authorization.service.UserService;
import com.company.backendcalculator.operations.dto.OperationEnum;
import com.company.backendcalculator.operations.dto.OperationResponse;
import com.company.backendcalculator.operations.entities.Operation;
import com.company.backendcalculator.operations.repository.OperationRepository;
import com.company.backendcalculator.operations.service.OperationService;
import com.company.backendcalculator.records.service.RecordService;
import com.company.backendcalculator.records.entities.Record;
import com.company.backendcalculator.authorization.entities.User;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.common.exceptions.Http500Exception;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;

@Service
public class OperationServiceImpl implements OperationService {

    private DecimalFormat decimalFormat = new DecimalFormat("#.#####");


    private static final String STRING_PROVIDER_URL = "https://www.random.org/strings/?num=1&len=8&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new";

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Override
    @Transactional
    public OperationResponse addition(UserData userData, Double operandOne, Double operandTwo){
        OperationCheckDto dto = checkUserCanPerformOperation(userData, OperationEnum.addition);

        Double result = operandOne + operandTwo;

        String strResult = asIntegerIfPossible(result);

        Double balance = saveAndGetBalance(userData, dto, strResult);

        return new OperationResponse(strResult,balance);
    }

    @Override
    @Transactional
    public OperationResponse substraction(UserData userData, Double operandOne, Double operandTwo){
        OperationCheckDto dto = checkUserCanPerformOperation(userData, OperationEnum.substraction);

        Double result = operandOne - operandTwo;

        String strResult = asIntegerIfPossible(result);

        Double balance = saveAndGetBalance(userData, dto, strResult);

        return new OperationResponse(strResult,balance);
    }

    @Override
    @Transactional
    public OperationResponse multiplication(UserData userData, Double operandOne, Double operandTwo){
        OperationCheckDto dto = checkUserCanPerformOperation(userData, OperationEnum.multiplication);

        Double result = operandOne * operandTwo;

        String strResult = asIntegerIfPossible(result);

        Double balance = saveAndGetBalance(userData, dto, strResult);

        return new OperationResponse(strResult,balance);
    }

    @Override
    @Transactional
    public OperationResponse division(UserData userData, Double operandOne, Double operandTwo){
        OperationCheckDto dto = checkUserCanPerformOperation(userData, OperationEnum.division);

        Double result = operandOne / operandTwo;

        String strResult = asIntegerIfPossible(result);

        Double balance = saveAndGetBalance(userData, dto, strResult);

        return new OperationResponse(strResult,balance);
    }

    @Override
    @Transactional
    public OperationResponse squareRoot(UserData userData, Double operandOne){
        OperationCheckDto dto = checkUserCanPerformOperation(userData, OperationEnum.square_root);

        Double result = Math.sqrt(operandOne);

        String strResult = asIntegerIfPossible(result);

        Double balance = saveAndGetBalance(userData, dto, strResult);

        return new OperationResponse(strResult,balance);
    }

    @Override
    @Transactional
    public OperationResponse randomString(UserData userData) {
        OperationCheckDto dto = checkUserCanPerformOperation(userData, OperationEnum.random_string);

        String result = getRandomString();

        Double balance = saveAndGetBalance(userData, dto, result);

        return new OperationResponse(result.toString(),balance);
    }


    @Override
    @Transactional
    public Double saveAndGetBalance(UserData userData, OperationCheckDto dto, String result){
        Operation operation = dto.getOperation();
        User user = dto.getUser();

        Double balance = user.getCurrentBalance() - operation.getCost();
        user.setCurrentBalance(balance);

        userService.save(userData,user);

        Record record = new Record();
        record.setAmount(operation.getCost());
        record.setOperationType(dto.getOperation().getType());
        record.setDate(ZonedDateTime.now());
        record.setUserBalance(user.getCurrentBalance());
        record.setUserId(user.getId());
        record.setOperationId(operation.getId());
        record.setOperation_response(result);
        record.setDeleted(false);

        recordService.create(userData,record);

        return balance;
    }

    @Override
    public OperationCheckDto checkUserCanPerformOperation(UserData userData, OperationEnum operationEnum){

        /*
        * NOTE:
        * I guess there is no sense in getting the operation record from the
        * database everytime it is needed.
        * I would add the cost value of each operation in a cache, to avoid
        * accesing the database everytime is needed
        * */
        Operation op = operationRepository.findByType(operationEnum.name());
        if( op == null){
            //oops
            throw new Http500Exception("operation not found");
        }

        Double cost = op.getCost();

        User user = userService.findById(userData.getUserId());

        if( user.getCurrentBalance() <= 0 ){
            throw new Http400Exception("User exceeded his/her balance");
        }

        if( cost > user.getCurrentBalance() ){
            throw new Http400Exception("User exceeded his/her balance");
        }
        return new OperationCheckDto(user, op);
    }

    @Override
    public String getRandomString()  {
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(STRING_PROVIDER_URL))
                    .GET()
                    .build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception ex){
            throw new Http500Exception("Could not contact random string provider");
        }

        boolean validResponse = response.statusCode() >= 200 && response.statusCode() < 300;
        if( !validResponse ){
            throw new Http500Exception("Error returned from random string provider");
        }

        String result = response.body();
        return result;
    }

     @Override
     public String asIntegerIfPossible(Double result){
         String strResult = decimalFormat.format(result);
         if( result.intValue() == result.doubleValue()){
             strResult = String.valueOf(result.intValue());
         }
         return  strResult;
     }
}
