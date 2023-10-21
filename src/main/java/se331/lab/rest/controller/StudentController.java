package se331.lab.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import se331.lab.rest.entity.Student;
import se331.lab.rest.service.StudentService;
import se331.lab.rest.util.LabMapper;

@Controller
@RequiredArgsConstructor
public class StudentController {
    final StudentService studentService;

    @GetMapping("students")
    public ResponseEntity<?> getAdvisorLists(@RequestParam(value = "_limit", required = false) Integer perPage,
                                             @RequestParam(value = "_page", required = false) Integer page,
                                             @RequestParam(value = "title", required = false) String title) {
        perPage = perPage == null ? 3 : perPage;
        page = page == null ? 1 : page;
        Page<Student> pageOutput;
        if(title == null){
            pageOutput  = studentService.getStudents(perPage, page);
        } else {
            pageOutput = studentService.getStudent(title, PageRequest.of(page -1, perPage));
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));
        return new ResponseEntity<>(LabMapper.INSTANCE.getStudentDto(pageOutput.getContent()),responseHeaders, HttpStatus.OK);
    }
    @GetMapping("students/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id")Long id){
       Student output = studentService.getStudentById(id);
        if(output != null){
            return ResponseEntity.ok(LabMapper.INSTANCE.getStudentDto(output));
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the given id is not found");
        }
    }
}
