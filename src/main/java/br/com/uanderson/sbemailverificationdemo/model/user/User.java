package br.com.uanderson.sbemailverificationdemo.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId(mutable = true)
    private String email;
    private String password;
    private String role;
    private boolean isEnabled = false;
}//class

/*
A anotação @NaturalId é usada para marcar propriedades de entidades
que devem ser consideradas como "identificadores naturais"
e são usadas como chaves únicas nas tabelas do banco de dados.

Aqui estão os detalhes da anotação @NaturalId no Hibernate:

    @NaturalId é usada para sinalizar que uma determinada propriedade de
    uma entidade representa um identificador natural.

    Um identificador natural é uma chave que tem significado semântico no contexto
    do domínio do aplicativo. Por exemplo, o número de passaporte de um indivíduo
    poderia ser um identificador natural em um aplicativo de gerenciamento de passaportes.

    Quando uma propriedade é marcada com @NaturalId, o Hibernate usa essa propriedade
    como um identificador natural no banco de dados.

    Por padrão, as propriedades marcadas com @NaturalId são consideradas como somente
    leitura, o que significa que o Hibernate assume que elas não devem ser modificadas
    após a criação da entidade no banco de dados. Isso é indicado pelo parâmetro mutable
    padrão, que é false.

    Se você definir mutable como true, o Hibernate permitirá que você atualize a
    propriedade marcada com @NaturalId.

 */