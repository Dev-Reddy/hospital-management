
package com.capgemini.hmsbackend.repository;

import com.capgemini.hmsbackend.dto.ProceduresDto;
import com.capgemini.hmsbackend.entity.Physician;
import com.capgemini.hmsbackend.entity.Procedures;
import com.capgemini.hmsbackend.entity.TrainedIn;
import com.capgemini.hmsbackend.entity.TrainedInId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainedInRepository extends JpaRepository<TrainedIn, TrainedInId> {

    @Query("""
        select new com.capgemini.hmsbackend.dto.ProceduresDto(
            p.code, p.name, p.cost
        )
        from TrainedIn ti
        join ti.procedure p
        where ti.id.physicianId = :physicianId
    """)
    List<ProceduresDto> findProceduresByPhysicianId(@Param("physicianId") Integer physicianId);

    @Query("SELECT t.physician FROM TrainedIn t WHERE t.procedure.code = :procedureId")
    List<Physician> findPhysiciansByProcedure(@Param("procedureId") int procedureId);



    @Modifying
    @Transactional
    @Query("""
        update TrainedIn ti
        set ti.certificationExpires = :expiryDate
        where ti.id.physicianId = :physicianId and ti.id.treatmentId = :procedureId
    """)
    int updateCertificationExpiry(@Param("physicianId") Integer physicianId,
                                  @Param("procedureId") Integer procedureId,
                                  @Param("expiryDate") LocalDateTime expiryDate);


    //code by sarvadnya
    @Query("SELECT t FROM TrainedIn t WHERE t.physician.employeeId = :physicianId AND t.certificationExpires <= :threshold")
    List<TrainedIn> findExpiredSoonByPhysician(@Param("physicianId") Integer physicianId, @Param("threshold") LocalDateTime threshold);

}
