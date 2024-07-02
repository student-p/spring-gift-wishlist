package gift.controller;

import gift.Product;
import gift.repository.ProductJdbcRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProductController {

    private final ProductJdbcRepository productjdbcRepository;

    @Autowired
    public ProductController(ProductJdbcRepository productRepository) {
        this.productjdbcRepository = productRepository;
    }


    // 상품 모두 조회
    @GetMapping("/api/products")
    public String responseAllProducts(Model model){
        List<Product> productsList = productjdbcRepository.findAll();
        model.addAttribute("products", productsList);
        return "index";
    }


    // 상품 추가 폼
    @GetMapping("/api/products/new-form")
    public String newProductForm(Model model) {
        return "new-product-form";
    }

    // 상품 추가
    @PostMapping("/api/products")
    public String addOneProduct(@ModelAttribute Product product) {
        productjdbcRepository.save(product);
        return "redirect:/api/products";
    }

    // 상품 수정 폼
    @GetMapping("/api/products/edit/{id}")
    public String editProductForm(@PathVariable("id") long id, Model model) {
        Product product = productjdbcRepository.findById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "modify-product-form";
        }
        return "redirect:/api/products";
    }

    // 상품 수정
    @PutMapping("/api/products/modify/{id}")
    public String modifyOneProduct(@PathVariable("id") long id, @ModelAttribute Product product) {
        productjdbcRepository.update(product);
        return "redirect:/api/products";
    }

    // 상품 삭제
    @DeleteMapping("/api/products/{id}")
    public String deleteOneProduct(@PathVariable("id") long id) {
        productjdbcRepository.deleteById(id);
        return "index";
    }

    // 선택된 상품 삭제
    @DeleteMapping("/api/products/delete-selected")
    public ResponseEntity<String> deleteSelectedProducts(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            productjdbcRepository.deleteById(id);
        }
        return new ResponseEntity<>("Selected products deleted successfully.", HttpStatus.OK);
    }
}
