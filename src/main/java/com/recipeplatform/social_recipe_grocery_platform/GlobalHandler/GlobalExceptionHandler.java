package com.recipeplatform.social_recipe_grocery_platform.GlobalHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.recipeplatform.social_recipe_grocery_platform.dtos.ApiErrorResponse;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.GroceryProductNotFound;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.RecipeNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.UserNotFoundException;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;



@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle user not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle grocery product not found
    @ExceptionHandler(GroceryProductNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(
            GroceryProductNotFound ex,
            WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    // Handle Recipts not found 
    @ExceptionHandler (RecipeNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRecipeNotFound(
         RecipeNotFoundException ex, WebRequest request
    ){ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    // Handle DTO validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message,
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.badRequest().body(response);
    }
}
