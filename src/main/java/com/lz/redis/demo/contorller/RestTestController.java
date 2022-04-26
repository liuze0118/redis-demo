package com.lz.redis.demo.contorller;

import com.lz.redis.demo.annotatiion.ApiVersion;
import com.lz.redis.demo.download.DownLoadFromUrl;
import com.lz.redis.demo.service.UserService;
import com.lz.redis.demo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{version}/rest")
public class RestTestController {
    @Autowired
    private UserService userService;
    @Autowired
    private DownLoadFromUrl downLoadFromUrl;
    @GetMapping("/user/{id}")
    public User queryUserById(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return user;
    }

    @GetMapping("/user/{id}")
    @ApiVersion(2)
    public User queryUserById2(@PathVariable Integer id){
        User user = new User();
        user.setId(5);
        user.setName("test");
        return user;
    }

    @ResponseBody
    @GetMapping("/users/search")
    public List<User> queryUserByName(String name,String nickName){
        User searchUser = new User();
        searchUser.setName(name);
        searchUser.setNickName(nickName);
        List<User> userList = userService.getUserByCondition(searchUser);
        return userList;
    }

    @PostMapping("/user")
    public void create(@RequestBody User user){
        userService.addUser(user);
    }

    @PutMapping("/user")
    public int update(@RequestBody User user){
        int i = 0;
        try {
            i = userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    @DeleteMapping("/user/{id}")
    public int delete(@PathVariable("id") int id){
        int i = userService.deleteUserById(id);
        return i;
    }
    @GetMapping("/downLoad")
    public void downLoad(@RequestParam("url")String url) throws Exception {
        downLoadFromUrl.downLoadPictures(url);
    }
    @PutMapping("/users")
    public int updates(@RequestBody User user){
        int i = 0;
        try {
            i = userService.noTransactional(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
