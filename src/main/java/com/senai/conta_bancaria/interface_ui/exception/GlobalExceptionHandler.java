package com.senai.conta_bancaria.interface_ui.exception;

import com.senai.conta_bancaria.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail badRequest(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Um ou mais campos são inválidos",
                request.getRequestURI()
        );

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Tipo de parâmetro inválido");
        problem.setDetail(String.format(
                "O parâmetro '%s' deve ser do tipo '%s'. Valor recebido: '%s'",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido",
                ex.getValue()
        ));
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ProblemDetail handleConversionFailed(ConversionFailedException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Falha de conversão de parâmetro");
        problem.setDetail("Um parâmetro não pôde ser convertido para o tipo esperado.");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("error", ex.getMessage());
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Erro de validação nos parâmetros");
        problem.setDetail("Um ou mais parâmetros são inválidos");
        problem.setInstance(URI.create(request.getRequestURI()));

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String campo = violation.getPropertyPath().toString();
            String mensagem = violation.getMessage();
            errors.put(campo, mensagem);
        });
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ContaDeMesmoTipoException.class)
    public ProblemDetail handleContaDeMesmoTipo(ContaDeMesmoTipoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Conta desse tipo já existe para este cliente.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex,
                                                     HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.NOT_FOUND,
                "Entidade não encontrada.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(RendimentoInvalidoException.class)
    public ProblemDetail handleRendimentoInvalido(RendimentoInvalidoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Rendimento inválido.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ProblemDetail handleSaldoInsuficiente(SaldoInsuficienteException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Saldo insuficiente.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TipoDeContaInvalidaException.class)
    public ProblemDetail handleTipoDeContaInvalido(TipoDeContaInvalidaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Tipo de conta inválido.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TransferenciaParaMesmaContaException.class)
    public ProblemDetail handleTransferenciaParaMesmaConta(TransferenciaParaMesmaContaException ex,
                                                           HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.CONFLICT,
                "Transferência para mesma conta não é permitida.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ValoresNegativosException.class)
    public ProblemDetail handleValoresNegativos(ValoresNegativosException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Valores negativos não são permitidos.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TaxaInvalidaException.class)
    public ProblemDetail handleTaxaInvalida(TaxaInvalidaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Taxa inválida.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(PagamentoInvalidoException.class)
    public ProblemDetail handlePagamentoInvalido(PagamentoInvalidoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Pagamento inválido.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BoletoVencidoException.class)
    public ProblemDetail handleBoletoVencido(BoletoVencidoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Boleto vencido.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AutenticacaoIoTExpiradaException.class)
    public ProblemDetail handleAutenticacaoIoTExpirada(AutenticacaoIoTExpiradaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.UNAUTHORIZED,
                "Autenticação IoT expirada.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DispositivoIoTInativoException.class)
    public ProblemDetail handleDispositivoIoTInativo(DispositivoIoTInativoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.FORBIDDEN,
                "Dispositivo IoT inativo.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro desconhecido.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }
}