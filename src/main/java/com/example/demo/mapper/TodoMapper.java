package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.demo.model.Todo;

public interface TodoMapper {

    List<Todo> findAll();

    List<Todo> findAllByCurrentUser(@Param("currentUser") String currentUser);

    Todo findById(@Param("id") Long id);

    int insert(Todo todo);

    int update(Todo todo);

    int deleteById(@Param("id") Long id);
}
