package com.university.ui.data.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.gentyref.TypeToken;
import com.university.ui.data.entity.Course;
import com.university.ui.data.entity.Person;
import com.university.ui.data.entity.StudentCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class StudentCourseServiceImpl implements StudentCourseService{
    @Autowired
    private final WebClient webClient;
    private final String URL="http://localhost:8080/api";
    @Autowired
    private CourseService courseService;

    public StudentCourseServiceImpl(WebClient webClient, CourseService courseService) {
        this.webClient = webClient;
        this.courseService = courseService;
    }

    @Override
    public Page<StudentCourse> list(Pageable pageable, String courseName, String studentIdnp) {
        Optional<Integer> courseId = null;

        if(!courseName.isEmpty() ){
            Page<Course> page = courseService.list(PageRequest.of(0, 1),Optional.of(courseName),Optional.ofNullable(null),Optional.ofNullable(null),Optional.ofNullable(null));
            if(page.hasContent()) courseId = Optional.ofNullable(page.getContent().get(0).getId());
        }
        if (studentIdnp!=null){
        Mono<String> responseMono = webClient.get()
                .uri(URL+"/studentCourse?studentIdnp="+studentIdnp + (courseId!=null && !courseId.isEmpty()  ? ("&courseId=" + courseId.get()) : (""))

                )
                .retrieve()
                .bodyToMono(String.class);

        String jsonResponse = responseMono.block();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Pageable.class, new PageableDeserializer())
                .create();

        Type pageResponseType = new TypeToken<PageResponse<StudentCourse>>() {}.getType();
        PageResponse<StudentCourse> pageResponse = gson.fromJson(jsonResponse, pageResponseType);

        List<StudentCourse> studentCourseList = pageResponse.getContent();
        Pageable pageable1 = pageResponse.getPageable();
        long totalElements = pageResponse.getTotalElements();
            System.out.println(studentCourseList);

        Page<StudentCourse> studentCoursePage = new PageImpl<>(studentCourseList, pageable1, totalElements);
        return studentCoursePage;}
        else return null;

    }

    @Override
    public byte update(StudentCourse studentCourse) {
        if(getStudentCourse(studentCourse.getCourseId(), studentCourse.getStudentIdnp())!=null) return 0;
        else{
            webClient.post()
                    .uri(URL+"/studentCourse")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(studentCourse)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();  // Așteaptă finalizarea request-ului și blochează execuția

            return 1;}
    }

    @Override
    public StudentCourse getStudentCourse(int courseId,String studentIdnp) {
        Mono<StudentCourse> studentCourseMono =  webClient.get()
                .uri(URL+"/studentCourse/"+courseId+"/"+studentIdnp)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<StudentCourse>() {});
        return studentCourseMono.block();
    }
}
