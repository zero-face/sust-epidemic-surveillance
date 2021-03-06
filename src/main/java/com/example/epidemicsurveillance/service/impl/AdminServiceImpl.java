package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.Admin;
import com.example.epidemicsurveillance.entity.AdminRole;
import com.example.epidemicsurveillance.entity.Role;
import com.example.epidemicsurveillance.entity.vo.AdminLoginVo;
import com.example.epidemicsurveillance.entity.vo.RegisterVo;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.example.epidemicsurveillance.mapper.AdminMapper;
import com.example.epidemicsurveillance.mapper.AdminRoleMapper;
import com.example.epidemicsurveillance.mapper.RoleMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.IAdminRoleService;
import com.example.epidemicsurveillance.service.IAdminService;
import com.example.epidemicsurveillance.utils.jwt.JwtTokenUtil;
import com.example.epidemicsurveillance.utils.oss.ALiYunOssUtil;
import com.example.epidemicsurveillance.utils.rabbitmq.EmailSendUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * ???????????? ???????????????
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailSendUtil emailSendUtil;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private IAdminRoleService iAdminRoleService;


    @Value("${jwt.tokenHead}")
    private String tokenHead;


    @Override
    public Admin getAdminByUsername(String username) {
        if(StringUtils.isBlank(username)){
            throw new EpidemicException("?????????????????????");
        }
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        Admin admin=adminMapper.selectOne(wrapper);
        if(admin == null){
            throw new EpidemicException("??????????????????");
        }
        return admin;
    }

    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    @Override
    public void getKaptcha(HttpServletRequest request, HttpServletResponse response, String key) {
        // ??????response???????????????image/jpeg??????
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        //-------------------??????????????? begin --------------------------
        //???????????????????????????
        String text=defaultKaptcha.createText();
        System.out.println("???????????????"+text);
        //??????????????????????????????redis  ??????????????????
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,text,5L, TimeUnit.MINUTES);
        //????????????????????????????????????????????????
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream=null;
        try {
            outputStream = response.getOutputStream();
            //?????????????????????,?????????jpg
            ImageIO.write(image, "jpg",outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream !=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //-------------------??????????????? end --------------------------
    }


    @Override
    public ResponseResult login(AdminLoginVo adminLoginVo) throws NoSuchAlgorithmException, InvalidKeySpecException {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        String kaptchaCode=(String) operations.get(adminLoginVo.getKaptchaCodeId());
        if(kaptchaCode == null){
            throw new EpidemicException("???????????????????????????????????????");
        }
        System.out.println(kaptchaCode);
        System.out.println(adminLoginVo.getKaptchaCode());
        if(!kaptchaCode.equals(adminLoginVo.getKaptchaCode())){
            throw new EpidemicException("???????????????????????????");
        }
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",adminLoginVo.getUsername());
        Admin admin=adminMapper.selectOne(wrapper);
        if(admin == null){
            throw new EpidemicException("??????????????????");
        }
        if(admin.getIsExamine() == 0){
            throw new EpidemicException("??????????????????????????????????????????");
        }
        if(!admin.getPassword().equals(adminLoginVo.getPassword())){
            throw new EpidemicException("????????????????????????");
        }
        //???????????????????????????token
        String token=tokenHead+jwtTokenUtil.generateToken(admin);
        admin.setToken(token);
        adminMapper.updateById(admin);
        //?????????????????????????????????security?????????,????????????
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(admin,null,admin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return ResponseResult.ok().data("token",token).data("tokenHead",tokenHead);
    }

    @Override
    public ResponseResult getAdminInfo(String token) {
        Admin admin=null;
        if(token != null && token.startsWith(tokenHead)){
            String truetoken=token.substring(tokenHead.length());
            String username=jwtTokenUtil.getUsernameFormToken(truetoken);
            //admin = (Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            QueryWrapper<Admin> wrapper=new QueryWrapper<>();
            wrapper.eq("username",username);
            admin=adminMapper.selectOne(wrapper);
            admin.setPassword(null);
            admin.setRoles(roleMapper.getRoles(admin.getId()));
        }
        return ResponseResult.ok().data("admin",admin);
    }

    @Override
    public void getEmailCode(String email) {
        Random random=new Random(999999);
        int code = random.nextInt(999999);
        if(code < 100000){
            code=code+100000;
        }
        System.out.println(code);
        String codeMessage="??????????????????"+code+",?????????????????????????????????10??????";
        emailSendUtil.sendEmailToAdmin(email,codeMessage);
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.getAndSet(email,String.valueOf(code));
        } catch (Exception e) {
            e.printStackTrace();
            throw new EpidemicException("???????????????????????????");
        }
    }

    @Override
    public ResponseResult register(RegisterVo admin) {
        String username=admin.getUsername();
        String phone=admin.getPhone();
        String email=admin.getEmail();
        String emailCode=admin.getCode();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String redisCode = (String) valueOperations.get(email);
        if(redisCode == null){
            throw new EpidemicException("??????????????????????????????????????????????????????");
        }
        if(!emailCode.equals(redisCode)){
            throw new EpidemicException("???????????????????????????");
        }
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        Admin admin1 = adminMapper.selectOne(wrapper);
        if(admin1 != null){
            throw new EpidemicException("????????????????????????");
        }else {
            QueryWrapper<Admin> wrapper1=new QueryWrapper<>();
            wrapper1.eq("email",email);
            Admin admin2 = adminMapper.selectOne(wrapper1);
            if(admin2 != null){
                throw new EpidemicException("?????????????????????");
            }else {
                QueryWrapper<Admin> wrapper2=new QueryWrapper<>();
                wrapper2.eq("phone",phone);
                Admin admin3 = adminMapper.selectOne(wrapper2);
                if(admin3 != null){
                    throw new EpidemicException("????????????????????????");
                }
            }
        }
        Admin result=new Admin();
        result.setEmail(email);
        result.setPassword(admin.getPassword());
        result.setPhone(phone);
        result.setUsername(username);
        adminMapper.insert(result);
        emailSendUtil.sendEmailToAdmin("2690534598@qq.com",admin.getUsername()+"?????????????????????");
        return ResponseResult.ok().message("???????????????????????????????????????????????????");
    }

    @Override
    public ResponseResult getAdmins(Integer type) {
        List<Admin> adminList=adminMapper.getAdmins(type);

        for (int i = 0; i <adminList.size() ; i++) {
           StringBuilder roleNames= new StringBuilder();
           List<Role> roleList=adminList.get(i).getRoles();
            for (int j = 0; j <roleList.size() ; j++) {
                if(j == 0){
                    roleNames.append(roleList.get(j).getRoleName());
                }else {
                    roleNames.append(",").append(roleList.get(j).getRoleName());
                }
            }
            adminList.get(i).setRoleNames(roleNames.toString());
        }
        return ResponseResult.ok().data("list",adminList);
    }

    @Override
    public ResponseResult getAdminById(Integer adminId) {
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("id",adminId);
        Admin admin = adminMapper.selectOne(wrapper);
        QueryWrapper<AdminRole> wrapper1=new QueryWrapper<>();
        wrapper1.eq("admin_id",adminId);
        List<AdminRole> adminRoles = adminRoleMapper.selectList(wrapper1);
        List<Integer> rolesIds=new LinkedList<>();
        for (AdminRole adminRole: adminRoles) {
            rolesIds.add(adminRole.getRoleId());
        }
        admin.setValue(rolesIds);
        return ResponseResult.ok().data("admin",admin);
    }

    @Override
    public ResponseResult addAdmin(Admin admin) {
        adminInformationJudgeForAdd(admin);
        adminMapper.insert(admin);
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",admin.getUsername());
        Admin newAdmin = adminMapper.selectOne(wrapper);
        Integer adminId=newAdmin.getId();
        //????????????
        List<Integer> roleIds = admin.getValue();
        List<AdminRole> adminRoleList=new LinkedList<>();
        for (Integer roleId: roleIds) {
            AdminRole adminRole=new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleList.add(adminRole);
        }
        iAdminRoleService.saveBatch(adminRoleList);
        return ResponseResult.ok().message("????????????");
    }

    @Override
    public ResponseResult updateAdmin(Admin admin) {
        adminInformationJudgeForUpdate(admin);
        //????????????
        Integer adminId=admin.getId();
        QueryWrapper<AdminRole> wrapper=new QueryWrapper<>();
        wrapper.eq("admin_id",adminId);
        adminRoleMapper.delete(wrapper);
        List<Integer> roleIds = admin.getValue();
        if(roleIds == null || roleIds.size() == 0){
            throw new EpidemicException("????????????????????????");
        }
        List<AdminRole> adminRoleList=new LinkedList<>();
        for (Integer roleId: roleIds) {
            AdminRole adminRole=new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleList.add(adminRole);
        }
        iAdminRoleService.saveBatch(adminRoleList);
        //??????
        adminMapper.updateById(admin);
        return ResponseResult.ok().message("????????????");
    }

    @Override
    public ResponseResult deleteAdmin(Integer adminId) {
        //????????????
        QueryWrapper<AdminRole> adminRoleQueryWrapper=new QueryWrapper<>();
        adminRoleQueryWrapper.eq("admin_id",adminId);
        adminRoleMapper.delete(adminRoleQueryWrapper);
        //?????????????????????
        Admin admin = adminMapper.selectById(adminId);
        String avatar=admin.getAvator();
        if(avatar != null){
            ALiYunOssUtil.deleteCoursePicture(avatar);
        }
        //????????????
        admin.setAvator(null);
        adminMapper.updateById(admin);
        adminMapper.deleteById(adminId);
        return ResponseResult.ok().message("????????????");
    }

    @Override
    public ResponseResult adminAdopt(Integer adminId) {
        QueryWrapper<AdminRole> wrapper=new QueryWrapper<>();
        wrapper.eq("admin_id",adminId);
        List<AdminRole> adminRoleList = adminRoleMapper.selectList(wrapper);
        if(adminRoleList == null || adminRoleList.size() == 0){
            throw new EpidemicException("?????????????????????????????????");
        }
        Admin admin = adminMapper.selectById(adminId);
        admin.setIsExamine(1);
        adminMapper.updateById(admin);
        emailSendUtil.sendEmailToAdmin(admin.getEmail(),"????????????SUST?????????????????????????????????????????????");
        return ResponseResult.ok().message("????????????");
    }

    @Override
    public ResponseResult adminFail(Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        emailSendUtil.sendEmailToAdmin(admin.getEmail(),"????????????SUST?????????????????????????????????????????????????????????????????????????????????");
        adminMapper.deleteById(adminId);
        return ResponseResult.ok().message("????????????");
    }


    private void adminInformationJudgeForAdd(Admin admin){
        if(StringUtils.isBlank(admin.getUsername()) || StringUtils.isBlank(admin.getEmail()) ||
                StringUtils.isBlank(admin.getPassword()) || StringUtils.isBlank(admin.getPhone())){
            throw new EpidemicException("?????????????????????????????????");
        }
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",admin.getUsername());
        Admin admin1 = adminMapper.selectOne(wrapper);
        if(admin1 != null){
            throw new EpidemicException("????????????????????????");
        }else {
            QueryWrapper<Admin> wrapper1=new QueryWrapper<>();
            wrapper1.eq("email",admin.getEmail());
            Admin admin2 = adminMapper.selectOne(wrapper1);
            if(admin2 != null){
                throw new EpidemicException("?????????????????????");
            }else {
                QueryWrapper<Admin> wrapper2=new QueryWrapper<>();
                wrapper2.eq("phone",admin.getPhone());
                Admin admin3 = adminMapper.selectOne(wrapper2);
                if(admin3 != null){
                    throw new EpidemicException("????????????????????????");
                }
            }
        }
    }

    private void adminInformationJudgeForUpdate(Admin admin){
        if(StringUtils.isBlank(admin.getUsername()) || StringUtils.isBlank(admin.getEmail()) ||
                StringUtils.isBlank(admin.getPassword()) || StringUtils.isBlank(admin.getPhone())){
            throw new EpidemicException("?????????????????????????????????");
        }
        QueryWrapper<Admin> wrapper=new QueryWrapper<>();
        wrapper.eq("username",admin.getUsername());
        Admin admin1 = adminMapper.selectOne(wrapper);
        if(admin1 != null && !admin1.getUsername().equals(admin.getUsername())){
            throw new EpidemicException("????????????????????????");
        }else {
            QueryWrapper<Admin> wrapper1=new QueryWrapper<>();
            wrapper1.eq("email",admin.getEmail());
            Admin admin2 = adminMapper.selectOne(wrapper1);
            if(admin2 != null && !admin2.getEmail().equals(admin.getEmail())){
                throw new EpidemicException("?????????????????????");
            }else {
                QueryWrapper<Admin> wrapper2=new QueryWrapper<>();
                wrapper2.eq("phone",admin.getPhone());
                Admin admin3 = adminMapper.selectOne(wrapper2);
                if(admin3 != null && !admin3.getPhone().equals(admin.getPhone())){
                    throw new EpidemicException("????????????????????????");
                }
            }
        }
    }
}
