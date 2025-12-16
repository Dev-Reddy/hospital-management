
package com.capgemini.hmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "appointment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"prescriptions"})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppointmentID")
    private Integer appointmentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "PrepNurse")
    private Nurse prepNurse;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Physician")
    private Physician physician;

    @Column(name = "Starto")
    private LocalDateTime starto;

    @Column(name = "Endo")
    private LocalDateTime endo;

  @Column(name = "ExaminationRoom", columnDefinition = "TEXT")
   private String examinationRoom;

    @OneToMany(mappedBy = "appointment")
    private List<Prescribes> prescriptions;

	public String getExaminationRoom() {
		// TODO Auto-generated method stub
		return this.examinationRoom;
	}

		
	}


