package priv.llf.commons.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: eleven
 * @since: 2018/4/18 22:21
 * @description:
 */
public class ModelMapperUtils {

    public static final ModelMapper mapper=new ModelMapper();


    public <T>List<T> mapList(Collections collections, Class<T> entitClass){

        List<T> resultList = new ArrayList<>();

        resultList = mapper.map(collections,new TypeToken<List<T>>(){}.getType());




        return resultList;
    }

}
