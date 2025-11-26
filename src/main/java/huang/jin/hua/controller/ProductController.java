package huang.jin.hua.controller;

import huang.jin.hua.pojo.Product;
import huang.jin.hua.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    double priceValue;
    int quantityValue;
    boolean isCall;

    @Autowired
    private ProductService productService;

    @GetMapping(value={"/product"})
    public ModelAndView product(){
        ModelAndView view = new ModelAndView("product");

        List<Product> productList = productService.getProducts();

        view.addObject("productList",productList);

        System.out.println(productList.get(0).getId());

        return view;
    }

    @GetMapping(value={"/addProduct"})
    public ModelAndView addProduct(){
        ModelAndView view = new ModelAndView("addProduct");
        return view;
    }

    @PostMapping("/addProduct")
    public ModelAndView login(String itemName, String price,String quantity,String descriptions) {
        ModelAndView view = new ModelAndView("addProduct");

        Product product = new Product();

        if(!price.isEmpty()){
            priceValue = Double.parseDouble(price);
            if(priceValue>200){
                view.addObject("error_price", "商品价格不能超过200元");
                view.addObject("callPrice", priceValue);
                isCall = true;
            }

        }

        if(!quantity.isEmpty()){
            quantityValue = Integer.parseInt(quantity);
            if(quantityValue>1000){
                view.addObject("error_quantity", "商品数量不能超过1000件");
                view.addObject("callQuantity",quantityValue);
                isCall = true;
            }

        }

        if (itemName.isEmpty() || price.isEmpty()  || quantity.isEmpty() || descriptions.isEmpty() ) {

            if(itemName.isEmpty()){
                view.addObject("error_name", "商品名不能为空");
            }
            if(price.isEmpty()){
                view.addObject("error_price", "商品价格不能为空");
            }
            if(quantity.isEmpty()){
                view.addObject("error_quantity", "商品数量不能为空");
            }
            if(descriptions.isEmpty()){
                view.addObject("error_descriptions", "商品描述不能为空");
            }
            return view;
        }

        if(isCall){
            return view;
        }

        product.setName(itemName);
        product.setPrice(priceValue);
        product.setQuantity(quantityValue);
        product.setDescriptions(descriptions);

        productService.addProduct(product);

        view.setViewName("redirect:/product");

        return view;
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateProduct(String id,String name,String price,String quantity){

        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty()) {

            if(name.isEmpty()){
                return "修改数据失败:商品名不能为空";
            }
            if(price.isEmpty()){
                return "修改数据失败:商品价格不能为空";
            }
            if(quantity.isEmpty()){
                return "修改数据失败:商品数量不能为空";
            }

        }

        if(!price.isEmpty()){
            priceValue = Double.parseDouble(price);
            if(priceValue>200){
                return "修改数据失败:商品价格不能超过200元";
            }
        }

        if(!quantity.isEmpty()){
            quantityValue = Integer.parseInt(quantity);
            if(quantityValue>1000){
                return "修改数据失败:商品数量不能超过1000件";
            }
        }

        //判断是否重名
        Product product2 = productService.getProductbyId(Integer.parseInt(id));
        System.out.println(product2.getName());
        System.out.println(name);
        if (product2.getName() != name){

            List<Product> product3 = productService.getProductbyName(name,Integer.parseInt(id));
            if (!product3.isEmpty()){
                return "修改数据失败:商品名不唯一";
            }

        }


        Product product = new Product();
        product.setId(Integer.parseInt(id));
        product.setName(name);
        product.setPrice(Double.parseDouble(price));
        product.setQuantity(Integer.parseInt(quantity));

        int isfinish = productService.updateProduct(product);

        if(isfinish==1){
            return "修改数据成功";
        }
        System.out.println(isfinish);

        return null;
    }


}
