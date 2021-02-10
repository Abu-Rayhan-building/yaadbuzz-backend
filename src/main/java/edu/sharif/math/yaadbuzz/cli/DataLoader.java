package edu.sharif.math.yaadbuzz.cli;

import edu.sharif.math.yaadbuzz.YaadbuzzApp;
import edu.sharif.math.yaadbuzz.config.ApplicationProperties;
import edu.sharif.math.yaadbuzz.config.StorageProperties;
import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.domain.User;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.MailService;
import edu.sharif.math.yaadbuzz.service.MemorialService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

public class DataLoader extends YaadbuzzApp {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    public DataLoader(Environment env) {
        super(env);
    }

    @Autowired
    public static void main(String[] args) {
        try {
            YaadbuzzApp.main(args);
            log.info("starting data loader");

            WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
            // for spring boot apps
            // ApplicationContext context =
            // SpringApplication.run(Application.class, args)
            UserService userService = (UserService) context.getBean("userService");
            MailService mailService = (MailService) context.getBean("mailService");
            MemorialService memorialService = (MemorialService) context.getBean("memorialService");
            DepartmentService departmentService = (DepartmentService) context.getBean("departmentService");

            var departmentDTO = new DepartmentDTO();
            DepartmentDTO departmentDto = departmentService.save(departmentDTO);

            File data = new File("data.csv");
            Scanner input = new Scanner(data);

            var line = input.nextLine().split(",");
            UserPerDepartmentDTO[] upd = new UserPerDepartmentDTO[line.length];
            var n = line.length;
            for (int i = 0; i < n; i++) {
                var userDTO = new AdminUserDTO();
                var user = userService.createUser(userDTO, null, Optional.empty());
                upd[i] = new UserPerDepartmentDTO();
            }
            for (int i = 0; i < n; i++) {
                line = input.nextLine().split(",");
                for (int j = 0; j < n; j++) {
                    var text = line[j];
                    var memorialDTO = new MemorialDTO();
                    memorialDTO.setDepartment(departmentDto);
                    memorialDTO.setRecipient(upd[j]);
                    memorialDTO.setWriter(upd[i]);
                    var commnetDTO = new CommentDTO();
                    commnetDTO.setWriter(upd[i]);
                    commnetDTO.setText(text);
                    memorialDTO.setNotAnonymousComment(commnetDTO);
                    MemorialDTO result = memorialService.save(memorialDTO);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
