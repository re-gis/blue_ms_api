package com.merci.blue.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.merci.blue.dtos.CreateExamDto;
import com.merci.blue.dtos.CreateResultDto;
import com.merci.blue.exceptions.ServiceException;

import java.io.IOException;

public class Mapper {
    public static ObjectMapper mapper = new ObjectMapper();

    public static CreateExamDto getDtoFromDetails(String string){
        try{
        return mapper.readValue(string, CreateExamDto.class);
        }catch (IOException e){
            throw new ServiceException("Error while mapping the details!");
        }
    }

    public static CreateResultDto getDtoFromDetailsForResult(String string){
        try{
            return mapper.readValue(string, CreateResultDto.class);
        }catch (IOException e){
            throw new ServiceException("Error while mapping the details!");
        }
    }
}
