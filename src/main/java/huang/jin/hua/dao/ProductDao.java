package huang.jin.hua.dao;

import huang.jin.hua.pojo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductDao {

    List<Product> getProducts();

    int addProduct(Product product);

    int updateProduct(Product product);

    Product getProductbyId(int i);

    List<Product> getProductbyName(String name,int id);
}
