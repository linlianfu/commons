package priv.llf.commons.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import priv.llf.commons.IMongo;
import priv.llf.mybatis.support.Page;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author: eleven
 * @since: 2017/10/21 13:03
 * @Description: 默认的集合名为类名首字母小写，所以当不传集合名字时，则类名必须和数据库集合名一致
 */
@Slf4j
public class MDSpringDaoTemplate<T extends Serializable>  implements IMongo,InitializingBean{
    /**
     * mongo数据的操作对象
     * 依赖注入,每个继承MDSpringDaoTemplate的类实例化后，调用set从spring IOC容器取该对象的实例注入
     */
    @Autowired
    @Getter
    MongoTemplate mt;

    private Class<T> entityClass;
    @Override
    public void save(Object entity) {
        this.mt.insert(entity);
    }

    @Override
    public Object findById(Object id) {
        return this.mt.findById(id,entityClass);
    }

    @Override
    public Page page(Page page, Query query) {
        if (page.getPageNo() <1){
            throw new RuntimeException("页码必须大于1");
        }
        if (page.getPageSize() < 1){
            throw new RuntimeException("每页获取的条数必须大于1");
        }
        long totalSize = this.countByQuery(query);
        double tempTotalPageSize = (double) totalSize/page.getPageSize();
        int totalPageSize = (int) tempTotalPageSize;
        if (totalPageSize < tempTotalPageSize){
            ++totalPageSize;
        }
        page.setTotalSize(totalSize);
        page.setTotalPageSize(totalPageSize);
        if (page.getPageNo() > totalPageSize){
            return  page;
        }
        query.skip((page.getPageNo()-1)*page.getPageSize());
        query.limit(page.getPageSize());
        List<T> resultList = this.getMt().find(query,entityClass);
        page.setCurrentPageStartIndex(query.getSkip()+1);
        page.setCurrentPageData(resultList);
        return page;
    }

    @Override
    public void update(Object entity) {
        this.mt.save(entity);
    }

    @Override
    public Object findByQuery(Query  query) {

        return this.mt.find(query,entityClass);
    }
    @Override
    public long countByQuery(Query query) {
        return this.getMt().count(query,entityClass);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        this.getMt().remove(query,entityClass);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        this.initEntityClassBean();//初始化实体bean

    }

    private void initEntityClassBean(){
        entityClass =  (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


}
