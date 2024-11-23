package com.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.Model.Claim;
import com.demo.Service.ClaimService;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/claim")
public class ClaimController {

	@Autowired
	private ClaimService claimService;

	@GetMapping("/all")
	public ResponseEntity<List<Claim>> getAllClaims() {
		List<Claim> claims = claimService.getAllClaims();
		return ResponseEntity.ok(claims);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Claim> getClaimDetailById(@PathVariable("id") Long id) {
		Optional<Claim> claim = claimService.findById(id);
		return claim.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/total")
	public ResponseEntity<Long> getTotalClaims() {
		Long total = claimService.getTotalClaims();
		return ResponseEntity.ok(total);
	}

	@GetMapping("/fetchMember/{id}")
	public ResponseEntity<Claim> getClaimByMemberId(@PathVariable("id") Long id) {
		Optional<Claim> claim = Optional.ofNullable(claimService.getClaimByMemberId(id));
		return claim.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/total/pending")
	public ResponseEntity<Long> getTotalPendingClaims() {
		Long total = claimService.getTotalPendingClaims("Pending");
		return ResponseEntity.ok(total);
	}

	@GetMapping("/total/approved")
	public ResponseEntity<Long> getTotalApprovedClaims() {
		Long total = claimService.getTotalApprovedClaims("Approved");
		return ResponseEntity.ok(total);
	}

	@PostMapping("/add")
	public ResponseEntity<Claim> save(@RequestBody Claim claim) {
		Claim savedClaim = claimService.addClaim(claim);
		return ResponseEntity.ok(savedClaim);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteClaim(@PathVariable("id") long id) {
		claimService.deleteClaimById(id);
		return ResponseEntity.noContent().build(); // 204 No Content
	}

	@PutMapping("/process/{id}")
	public ResponseEntity<Claim> processClaim(@PathVariable("id") Long id, @RequestBody Claim newClaim) {
		Optional<Claim> optional = claimService.findById(id);

		if (!optional.isPresent()) {
			return ResponseEntity.notFound().build(); // 404 Not Found
		}

		Claim oldClaim = optional.get();
		oldClaim.setClaimStatus(newClaim.getClaimStatus());

		Claim updatedClaim = claimService.addClaim(oldClaim);
		return ResponseEntity.ok(updatedClaim); // 200 OK
	}
}
