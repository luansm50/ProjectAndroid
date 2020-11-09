package com.cast.test.cadastro.curso.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Categoria implements Serializable
{
    private String codigo;
    private String descricao;
}
