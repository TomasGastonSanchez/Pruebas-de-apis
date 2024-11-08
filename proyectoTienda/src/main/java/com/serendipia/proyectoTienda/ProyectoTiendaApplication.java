package com.serendipia.proyectoTienda;

import com.serendipia.proyectoTienda.Servicios.CustomUserDetailsService;
import com.serendipia.proyectoTienda.Servicios.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EntityScan(basePackages = "Entidades")
@SpringBootApplication(scanBasePackages = "com.serendipia.proyectoTienda")
public class ProyectoTiendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoTiendaApplication.class,args);


	}
	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig {

		@Autowired
		private CustomUserDetailsService userDetailsService;


		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			http
					.csrf(csrf -> csrf.disable())  // Deshabilitar CSRF si es necesario
					.authorizeRequests(authz -> authz
							.requestMatchers(HttpMethod.POST, "/api/user").permitAll()  // Permitir acceso a /api/user sin autenticación
							.requestMatchers(HttpMethod.POST, "/api/login").permitAll() // Permitir acceso a /api/login sin autenticación
							.requestMatchers("/api/usuario/**").hasAuthority("ROLE_USER")  // Solo los usuarios con el rol ROLL_USER pueden acceder
							.requestMatchers("/api/protegida").authenticated()  // Requiere autenticación
							.requestMatchers("/api/token-requerido").authenticated() // Requiere autenticación

							//.requestMatchers("/api/").authenticated()// Requiere autenticación
							.anyRequest().authenticated()  // Todas las demás rutas requieren autenticación
					)
					//.httpBasic(withDefaults())  // Habilita autenticación básica
					// Añadir cualquier otro filtro personalizado como JWT
					.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

			return http.build();
		}

		@Bean
		public JWTAuthorizationFilter JWTAuthorizationFilter() {

			return new JWTAuthorizationFilter ();
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder(); // Usando BCrypt como ejemplo
		}


	}
}
////http://26.198.47.254:8082/login.do?jsessionid=e4fcda605db7ddafab790ab371937f39 (link a h2)
//jdbc:h2:tcp://localhost/~/hola
//https://medium.com/eduesqui/como-utilizar-la-base-de-datos-h2-en-spring-boot-db1241d1f7f3
//jdbc:h2:mem:testdb
//http://localhost:8080/api/nombre del get
//{
//    "Nombre":"sa",
//    "Apellido":"sa",
//    "Localidad":"rosario",
//    "Telefono": 123
//    }

