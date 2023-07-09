package com.university.ui.data.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.gentyref.TypeToken;
import com.university.ui.data.entity.Course;
import com.university.ui.data.entity.Person;
import org.hibernate.annotations.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    private final WebClient webClient;
    private final String URL="http://localhost:8080/api";

    public CourseServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Course> getAll() {
        Flux<Course> courses =  webClient.get()
                .uri(URL+"/courses")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Course>() {});
        return courses.collectList().block();
    }

    @Override
    public Page<Course> list(Pageable pageable) {

        Mono<String> responseMono = webClient.get()
                .uri(URL+"/courses?page="+pageable.getPageNumber()+"&size="+pageable.getPageSize()+"&sortBy="+pageable.getSort().toString())
                .retrieve()
                .bodyToMono(String.class);
        String jsonResponse = responseMono.block();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Pageable.class, new PageableDeserializer())
                .create();

        Type pageResponseType = new TypeToken<PageResponse<Course>>() {}.getType();
        PageResponse<Course> pageResponse = gson.fromJson(jsonResponse, pageResponseType);

        List<Course> userList = pageResponse.getContent();
        Pageable pageable1 = pageResponse.getPageable();
        long totalElements = pageResponse.getTotalElements();

        Page<Course> page = new PageImpl<>(userList, pageable1, totalElements);
        return page;
    }

    @Override
    public Page<Course> list(Pageable pageable, Optional<String> name, Optional<String> numStudent, Optional<String > numCredit, Optional<String> numHours) {

        Mono<String> responseMono = webClient.get()
                .uri(URL+"/courses?page="+pageable.getPageNumber()+"&size="+pageable.getPageSize()
                        +(name.isPresent() && !name.get().isEmpty() ? ("&name="+name.get()) : (""))
                        +(numStudent.isPresent() && !numStudent.get().isEmpty() ? ("&numStudent="+numStudent.get()) : "")
                        +(numCredit.isPresent() && !numCredit.get().isEmpty() ? ("&numCredit="+numCredit.get()) : "")
                        +(numHours.isPresent() && !numHours.get().isEmpty() ? ("&numHours="+numHours.get()) : "")

                )
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(URL+"/courses?page="+pageable.getPageNumber()+"&size="+pageable.getPageSize()+"&sortBy="+pageable.getSort().toString()
                +(name.isPresent() && !name.get().isEmpty() ? ("&name="+name.get()) : (""))
                +(numStudent.isPresent() && !numStudent.get().isEmpty() ? ("&numStudent="+numStudent.get()) : "")
                +(numCredit.isPresent() && !numCredit.get().isEmpty() ? ("&numCredit="+numCredit.get()) : "")
                +(numHours.isPresent() && !numHours.get().isEmpty() ? ("&numHours="+numHours.get()) : ""));
        String jsonResponse = responseMono.block();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Pageable.class, new PageableDeserializer())
                .create();

        Type pageResponseType = new TypeToken<PageResponse<Course>>() {}.getType();
        PageResponse<Course> pageResponse = gson.fromJson(jsonResponse, pageResponseType);

        List<Course> userList = pageResponse.getContent();
        Pageable pageable1 = pageResponse.getPageable();
        long totalElements = pageResponse.getTotalElements();

        Page<Course> page = new PageImpl<>(userList, pageable1, totalElements);
        return page;
    }


    @Override
    public void update(Course course) {

        if(getCourse(course.getId())!=null){
            webClient.put()
                    .uri(URL+"/courses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(course)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();  // Așteaptă finalizarea request-ului și blochează execuția

            System.out.println("Course update successfully!");}
        else{
            webClient.post()
                    .uri(URL+"/courses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(course)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();  // Așteaptă finalizarea request-ului și blochează execuția

            System.out.println("Course created successfully!");}
    }

    @Override
    public Course getCourse(int id ) {
        Mono<Course> course =  webClient.get()
                .uri(URL+"/courses/"+id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Course>() {});
        return course.block();
    }

    @Override
    public void delete(int id) {
        webClient.method(HttpMethod.DELETE)
                .uri(URL+"/courses/"+id)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();

        System.out.println("Course with id  "+id+" was deleted");
    }

    @Override
    public long count() {
        Mono<String> responseMono = webClient.get()
                .uri(URL+"/courses")
                .retrieve()
                .bodyToMono(String.class);
        String jsonResponse = responseMono.block();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Pageable.class, new PageableDeserializer())
                .create();

        Type pageResponseType = new TypeToken<PageResponse<Course>>() {}.getType();
        PageResponse<Course> pageResponse = gson.fromJson(jsonResponse, pageResponseType);

        long totalElements = pageResponse.getTotalElements();

        return totalElements;
    }

}
