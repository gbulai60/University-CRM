package com.university.ui.data.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.gentyref.TypeToken;
import com.university.ui.configuration.LocalDateTypeAdapter;
import com.university.ui.data.Role;
import com.university.ui.data.entity.Course;
import com.university.ui.data.entity.Person;
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
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonServiceImpl implements PersonService {

    private final WebClient webClient;
    private final String URL="http://localhost:8080/api/persons";

    public PersonServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void update(Person person) {

     if(getPerson(person.getIdnp())!=null){

         webClient.put()
                 .uri(URL)
                 .contentType(MediaType.APPLICATION_JSON)
                 .bodyValue(person)
                 .retrieve()
                 .bodyToMono(Void.class)
                 .block();  // Așteaptă finalizarea request-ului și blochează execuția

         System.out.println("Person update successfully!");}
     else{
        webClient.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(person)
                .retrieve()
                .bodyToMono(Void.class)
                .block();  // Așteaptă finalizarea request-ului și blochează execuția

        System.out.println("Person created successfully!");}
    }

    @Override
    public void delete(String idnp) {
        webClient.method(HttpMethod.DELETE)
                .uri(URL+"/"+idnp)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();

        System.out.println("Person with idnp  "+idnp+" was deleted");
    }

    @Override
    public Person getPerson(String idnp) {
        Mono<Person> person =  webClient.get()
                .uri(URL+"/"+idnp)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Person>() {});
        return person.block();
    }

    @Override
    public Page<Person> list(Pageable pageable, Optional<String> firstName, Optional<String> lastName, Optional<String> startDate, Optional<String> endDate, Optional<Set<Role>> roles) {

        String role="";
        if(roles.isPresent() && !roles.get().isEmpty()){
            for (Role role1:roles.get())
                role+="&role="+role1.name();
        }
        Mono<String> responseMono = webClient.get()
                .uri(URL+"?page="+pageable.getPageNumber()+"&size="+pageable.getPageSize()
                        + (firstName.isPresent() && !firstName.get().isEmpty() ? ("&firstName=" + firstName.get()) : (""))
                        + (lastName.isPresent() && !lastName.get().isEmpty() ? ("&lastName=" + lastName.get()) : "")
                        + (startDate.isPresent() && !startDate.isEmpty() ? ("&startDate=" + startDate.get()) : "")
                        + (endDate.isPresent() && !endDate.isEmpty() ? ("&endDate=" + endDate.get()) : "")
                        + role
                )
                .retrieve()
                .bodyToMono(String.class);
        String jsonResponse = responseMono.block();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(Pageable.class, new PageableDeserializer())
                .create();

        Type pageResponseType = new TypeToken<PageResponse<Person>>() {}.getType();
        PageResponse<Person> pageResponse = gson.fromJson(jsonResponse, pageResponseType);
        List<Person> personList = pageResponse.getContent();
        Pageable pageable1 = pageResponse.getPageable();
        long totalElements = pageResponse.getTotalElements();
        Page<Person> page = new PageImpl<>(personList, pageable1, totalElements);
        return page;
    }


}