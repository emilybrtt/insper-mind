package br.insper.insperMind.eletiva;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Eletiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}