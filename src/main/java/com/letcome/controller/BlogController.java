package com.letcome.controller;

import com.letcome.model.BlogEntity;
import com.letcome.model.UserEntity;
import com.letcome.repository.BlogRepository;
import com.letcome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    //查看所有博文
    @RequestMapping(value = "/admin/blogs.html", method = RequestMethod.GET)
    public String showBlogs(ModelMap modelMap){
        List<BlogEntity> blogList = blogRepository.findAll();
        modelMap.addAttribute("blogList",blogList);
        return "admin/blogs";
    }

    //添加博文
    @RequestMapping(value = "/admin/blogs/add", method = RequestMethod.GET)
    public String addBlog(ModelMap modelMap){
        List<UserEntity> userList = userRepository.findAll();
        //向jsp注入用户列表
        modelMap.addAttribute("userList", userList);
        return "admin/addBlog";
    }

    //添加博文，POST请求，重定向为查看博客页面
    @RequestMapping(value = "/admin/blogs/addP", method = RequestMethod.POST)
    public String addBlogPost(@ModelAttribute("blog") BlogEntity blogEntity){
        System.out.println(blogEntity.getTitle());
        System.out.println(blogEntity.getUserByUserId().getNickname());
        //存库
        blogRepository.saveAndFlush(blogEntity);
        //重定向地址
        return "redirect:/admin/blogs.html";
    }

    // 查看博文详情，默认使用GET方法时，method可以缺省
    @RequestMapping("/admin/blogs/show/{id}.html")
    public String showBlog(@PathVariable("id") int id, ModelMap modelMap){
        BlogEntity blog = blogRepository.findOne(id);
        modelMap.addAttribute("blog",blog);
        return "admin/blogDetail";
    }

    //修改博文内容，页面
    @RequestMapping("/admin/blogs/update/{id}.html")
    public String updateBlog(@PathVariable("id") int id, ModelMap modelMap){
        //是不是和上面那个方法很像
        BlogEntity blog = blogRepository.findOne(id);
        List<UserEntity> userList = userRepository.findAll();
        modelMap.addAttribute("blog", blog);
        modelMap.addAttribute("userList", userList);
        return "admin/updateBlog";
    }

    //修改博客内容，POST请求
    @RequestMapping(value = "/admin/blogs/updateP", method = RequestMethod.POST)
    public String updateBlogP(@ModelAttribute("blogP") BlogEntity blogEntity)
    {
        blogRepository.updateBlog(blogEntity.getTitle(), blogEntity.getUserByUserId().getId(), blogEntity.getContent(), blogEntity.getPubDate(), blogEntity.getId());
        blogRepository.flush();
        return "redirect:/admin/blogs.html";
    }

    // 删除博客文章
    @RequestMapping("/admin/blogs/delete/{id}")
    public String deleteBlog(@PathVariable("id") int id){
        blogRepository.delete(id);
        blogRepository.flush();
        return "redirect:/admin/blogs.html";
    }
}
