
package com.capgemini.hmsbackend.controller;
import com.capgemini.hmsbackend.dto.PhysicianDTO;
import com.capgemini.hmsbackend.entity.OnCall;
import com.capgemini.hmsbackend.dto.AffiliatedWithDTO;
import com.capgemini.hmsbackend.dto.DepartmentDTO;
import com.capgemini.hmsbackend.service.IAffiliatedWithService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/affiliated_with")
public class AffiliatedWithController {
    private final IAffiliatedWithService affiliatedWithService;
    public AffiliatedWithController(IAffiliatedWithService affiliatedWithService) {
        this.affiliatedWithService = affiliatedWithService;
    }
    @GetMapping("/primary/{physicianid}")
    public ResponseEntity<Boolean> updatePrimaryAffiliation(@PathVariable("physicianid") Integer physicianId) {
        boolean updated = affiliatedWithService.updatePrimaryAffiliation(physicianId);
        return ResponseEntity.ok(updated);
    }

    // By sahithi
    @GetMapping("/physicians/{deptid}")
    public ResponseEntity<List<PhysicianDTO>> getPhysiciansByDepartment(@PathVariable int deptid) {
        return ResponseEntity.ok(affiliatedWithService.getPhysiciansByDepartment(deptid));
    }

    @GetMapping("/countphysician/{deptid}")
    public ResponseEntity<Object> countPhysicians(@PathVariable int deptid) {
        int response = affiliatedWithService.getPhysicianCount(deptid);
        return ResponseEntity.ok(response);
    }

    //code by sarvadnya

    @GetMapping("/department/{physicianid}")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsByPhysician(@PathVariable Integer physicianid) {
        return ResponseEntity.ok(affiliatedWithService.getDepartmentsByPhysician(physicianid));
    }
   //code by Arshiya
   @PostMapping("/post")
   public ResponseEntity<String> addAffiliation(@RequestBody AffiliatedWithDTO dto) {
       affiliatedWithService.addAffiliation(dto);
       return ResponseEntity.ok("Affiliation Added Successfully");
   }
}
