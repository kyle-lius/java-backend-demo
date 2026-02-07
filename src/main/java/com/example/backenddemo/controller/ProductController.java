package com.example.backenddemo.controller;

import com.example.backenddemo.entity.Product;
import com.example.backenddemo.service.ProductService;
//import com.example.backenddemo.util.JwtValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

//    @Autowired
//    private JwtValidationHelper jwtValidationHelper;

    /**
     * 根据ID查询产品
     */
    @GetMapping("/{id}")
    public Map<String, Object> getProductById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Product product = productService.getProductById(id);
            if (product != null) {
                result.put("success", true);
                result.put("data", product);
            } else {
                result.put("success", false);
                result.put("message", "产品不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 查询所有产品
     */
    @GetMapping("/all")
    public Map<String, Object> getAllProducts() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Product> products = productService.getAllProducts();
            result.put("success", true);
            result.put("data", products);
            result.put("count", products.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询产品
     */
    @GetMapping("/page")
    public Map<String, Object> getProductsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Product> products = productService.getProductsByPage(page, size);
            int totalCount = productService.getProductCount();
            result.put("success", true);
            result.put("data", products);
            result.put("totalCount", totalCount);
            result.put("currentPage", page);
            result.put("pageSize", size);
            result.put("totalPages", (int) Math.ceil((double) totalCount / size));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 按条件搜索产品
     */
    @GetMapping("/search")
    public Map<String, Object> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Product> products = productService.searchProducts(name, category, status);
            result.put("success", true);
            result.put("data", products);
            result.put("count", products.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 新增产品
     */
    @PostMapping
    public Map<String, Object> createProduct(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = productService.createProduct(product);
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "产品创建成功");
                result.put("data", product);
            } else {
                result.put("success", false);
                result.put("message", "产品创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 更新产品
     */
    @PutMapping
    public Map<String, Object> updateProduct(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = productService.updateProduct(product);
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "产品更新成功");
            } else {
                result.put("success", false);
                result.put("message", "产品更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 删除产品（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProduct(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = productService.deleteProduct(id);
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "产品删除成功");
            } else {
                result.put("success", false);
                result.put("message", "产品删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 上架产品
     */
    @PutMapping("/{id}/enable")
    public Map<String, Object> enableProduct(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = productService.enableProduct(id);
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "产品上架成功");
            } else {
                result.put("success", false);
                result.put("message", "产品上架失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 下架产品
     */
    @PutMapping("/{id}/disable")
    public Map<String, Object> disableProduct(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = productService.disableProduct(id);
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "产品下架成功");
            } else {
                result.put("success", false);
                result.put("message", "产品下架失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取受保护的产品数据（需要JWT验证）
     */
    @GetMapping("/protected-data")
    public Map<String, Object> getProtectedData(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        
        // 使用公共JWT验证方法验证令牌
        Map<String, Object> validation = null;//jwtValidationHelper.validateTokenAndGetUserInfo(token);
        
        if (!(Boolean) validation.get("valid")) {
            result.put("success", false);
            result.put("message", validation.get("message"));
            result.put("code", validation.get("code"));
            return result;
        }
        
        String username = (String) validation.get("username");
        try {
            // 返回受保护的数据
            List<Product> products = productService.getAllProducts();
            result.put("success", true);
            result.put("data", products);
            result.put("count", products.size());
            result.put("message", "获取受保护数据成功");
            result.put("user", username); // 显示当前用户
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取受保护数据失败: " + e.getMessage());
        }
        return result;
    }
}