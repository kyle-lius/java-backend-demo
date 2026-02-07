package com.example.backenddemo.service;

import com.example.backenddemo.entity.Product;
import com.example.backenddemo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 根据ID查询产品
     */
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }

    /**
     * 查询所有产品
     */
    public List<Product> getAllProducts() {
        return productMapper.selectAll();
    }

    /**
     * 分页查询产品
     */
    public List<Product> getProductsByPage(int page, int size) {
        int offset = (page - 1) * size;
        return productMapper.selectByPage(offset, size);
    }

    /**
     * 按条件搜索产品
     */
    public List<Product> searchProducts(String name, String category, Integer status) {
        return productMapper.selectByCondition(name, category, status);
    }

    /**
     * 新增产品
     */
    public int createProduct(Product product) {
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        return productMapper.insert(product);
    }

    /**
     * 更新产品
     */
    public int updateProduct(Product product) {
        product.setUpdateTime(LocalDateTime.now());
        return productMapper.update(product);
    }

    /**
     * 删除产品（逻辑删除）
     */
    public int deleteProduct(Long id) {
        return productMapper.updateStatus(id, 0); // 设置状态为0表示删除
    }

    /**
     * 上架产品
     */
    public int enableProduct(Long id) {
        return productMapper.updateStatus(id, 1); // 设置状态为1表示上架
    }

    /**
     * 下架产品
     */
    public int disableProduct(Long id) {
        return productMapper.updateStatus(id, 0); // 设置状态为0表示下架
    }

    /**
     * 获取产品总数
     */
    public int getProductCount() {
        return productMapper.count();
    }
}