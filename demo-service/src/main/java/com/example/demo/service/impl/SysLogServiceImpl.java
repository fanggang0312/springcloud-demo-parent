package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.SysLogMapper;
import com.example.demo.model.SysLog;
import com.example.demo.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * <p>
 * 系统操作日志表 服务实现类
 * </p>
 *
 * @author fg
 * @since 2020/11/09
 */
@Service
@Slf4j
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public void insertSysLog(SysLog sysLogAdd) {
        sysLogMapper.insert(sysLogAdd);
    }

    private static String path = "C:\\uploadFile";
    @Override
    public String doUploadFile(HttpServletRequest request, HttpServletResponse response) {
        File file = new File(path);
        //判断上传文件的保存目录是否存在
        if(!file.exists() && !file.isDirectory()){
            file.mkdir();
        }
        try{
            //创建DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //创建文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //解决中文乱码
            upload.setHeaderEncoding("UTF-8");
            //保存的文件名
            String fileName = request.getParameter("fileName");
            try (InputStream in = request.getInputStream(); FileOutputStream out = new FileOutputStream(path + "\\" + fileName)) {
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            } catch (IOException e) {
                log.error("中国银行文件传输--上传文档过程中异常", e);
                return "上传文档过程中异常!";
            }
        }catch(Exception e){
            log.error("中国银行文件传输--上传文档过程中异常", e);
            return "上传文档过程中异常!";
        }
        return "上传成功!";
    }

    @Override
    public void doDownloadFile(HttpServletRequest request, HttpServletResponse response) {
        //下载的文件名
        String fileName = request.getParameter("fileName");
        BufferedInputStream bis = null;
        OutputStream ops = null;
        try {
            File dataInfFile = new File(path+"\\"+fileName);
            InputStream fileInputStream = new FileInputStream(dataInfFile);
            int contentLength = fileInputStream.available();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            response.addHeader("Content-Length", String.valueOf(contentLength));
            response.setContentType("application/octet-stream");
            ops = response.getOutputStream();
            bis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bis.read(buffer)) > 0) {
                ops.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("中国银行文件传输--下载文档过程中异常", e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (ops != null) {
                    ops.flush();
                    ops.close();
                }
            } catch (IOException e) {
                log.error("中国银行文件传输--关闭流过程中异常", e);
            }
        }

    }
}
