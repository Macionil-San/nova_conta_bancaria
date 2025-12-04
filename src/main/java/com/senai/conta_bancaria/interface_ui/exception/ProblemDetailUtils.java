package com.senai.conta_bancaria.interface_ui.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class ProblemDetailUtils {
    public static ProblemDetail buildProblem(HttpStatus status, String title, String detail, String path) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setInstance(URI.create(path));
        return problem;
    }
}