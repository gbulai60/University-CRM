package com.university.ui.data.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.gentyref.TypeToken;
import com.university.ui.data.entity.Group;
import com.university.ui.data.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.lang.reflect.Type;
import java.util.*;


@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private final WebClient webClient;
    private final String URL="http://localhost:8080/api";


    public StudentServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }
    public List<Group> getAllGroups(){
        Flux<Group> grupe =  webClient.get()
                .uri(URL+"/groups")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Group>() {});
        return grupe.collectList().block();
    }


    @Override
    public Student getByIdnp(String idnp) {
        Mono<Student> student =  webClient.get()
                .uri(URL+"/students/"+idnp)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Student>() {});
        return student.block();
    }

    public void update(Student object, String path) {

        webClient.post()
                .uri(URL+path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(object)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }

    @Override
    public void delete(String id) {
        webClient.method(HttpMethod.DELETE)
                .uri(URL+"/students/"+id)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();

        System.out.println("Student with idnp  "+id+" was deleted");
    }

    @Override
    public Page<Student> list(Pageable pageable, Optional<Group> group) {
        Mono<String> responseMono = webClient.get()
                .uri(URL+"/students?page="+pageable.getPageNumber()+"&size="+pageable.getPageSize()
                        + (group.isPresent()  ? ("&groupId=" + group.get().getId()) : (""))
                )
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(URL+"/students?page="+pageable.getPageNumber()+"&size="+pageable.getPageSize()
                + (group.isPresent()  ? ("&groupId=" + group.get().getId()) : ("")));
        String jsonResponse = responseMono.block();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Pageable.class, new PageableDeserializer())
                .create();

        Type pageResponseType = new TypeToken<PageResponse<Student>>() {}.getType();
        PageResponse<Student> pageResponse = gson.fromJson(jsonResponse, pageResponseType);

        List<Student> studentList = pageResponse.getContent();
        Pageable pageable1 = pageResponse.getPageable();
        long totalElements = pageResponse.getTotalElements();

        Page<Student> page = new PageImpl<>(studentList, pageable1, totalElements);
        return page;
    }

   }
