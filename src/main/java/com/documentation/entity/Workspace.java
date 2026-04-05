package com.documentation.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workspace")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;


    @ManyToOne
    @JoinColumn(name = "ownerId")
    private User owner;
    
    @OneToMany(mappedBy="workspace",cascade=CascadeType.ALL)
    private List<Folder> folders;
}