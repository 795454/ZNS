package huang.jin.hua.service;

import huang.jin.hua.dao.ProductDao;
import huang.jin.hua.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<Product> getProducts(){
        return productDao.getProducts();
    }

    public void addProduct(Product product) {
        productDao.addProduct(product);
    }

    public int updateProduct(Product product) {
        return productDao.updateProduct(product);
    }

    public Product getProductbyId(int i) {
        return productDao.getProductbyId(i);
    }

    public List<Product> getProductbyName(String name, int id) {
        return productDao.getProductbyName(name,id);
    }
}
