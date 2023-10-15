package br.com.andrelopes.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.andrelopes.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            //pega autenticação
            var servLetPath = request.getServletPath();
            if(servLetPath.startsWith("/tasks/")){

                var authorization = request.getHeader("Authorization");
                var authEncoded = authorization.substring("Basic".length()).trim() ;
                byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
                var authString = new String(authDecoded);
                String[] credentials = authString.split(":");
                var username = credentials[0];
                var password = credentials[1];
                System.out.println("authorization");
                System.out.println(username+ " " + password);       
                // validar usuario
                var user = this.userRepository.findByUserName(username);
                if(user == null){
                    response.sendError(HttpStatus.UNAUTHORIZED.value());
                    return;
                   }else{
                       //validar senha
                       var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());
                       if(passwordVerify.verified){
                          request.setAttribute("userName", user.getId());
                          filterChain.doFilter(request, response);
                       }else{
                           response.sendError(HttpStatus.UNAUTHORIZED.value());
                           return;
                       }
                       
                   }
            } else {
                filterChain.doFilter(request, response);
            }
 
    }       
}