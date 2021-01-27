package com.MadHatter.Athena.controllers

import com.MadHatter.Athena.commands.CreateMaintainerCommand
import com.MadHatter.Athena.commands.GetAllMaintainersCommand
import com.MadHatter.Athena.commands.GetMaintainerCommand
import com.MadHatter.Athena.commands.UpdateMaintainerCommand
import com.MadHatter.Athena.controllers.webModels.ErrorResponse
import com.MadHatter.Athena.controllers.webModels.MaintainersWebModel
import com.MadHatter.Athena.controllers.webModels.NewMaintainer
import com.MadHatter.Athena.models.MaintainerPojo
import com.MadHatter.Athena.commands.ImportMaintainerCsvCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/maintainers")
class MaintainersController {

  @Autowired
  lateinit var importMaintainerCsvCommand: ImportMaintainerCsvCommand

  @Autowired
  lateinit var getAllMaintainersCommand: GetAllMaintainersCommand

  @Autowired
  lateinit var createMaintainerCommand: CreateMaintainerCommand

  @Autowired
  lateinit var updateMaintainerCommand: UpdateMaintainerCommand

  @Autowired
  lateinit var getMaintainerCommand: GetMaintainerCommand

  @PreAuthorize("#oauth2.hasScope('madhatter.entity.read')")
  @GetMapping(produces = ["application/json"])
  fun getAllMaintainers(): MaintainersWebModel = MaintainersWebModel(getAllMaintainersCommand.execute())

  @PreAuthorize("#oauth2.hasScope('madhatter.entity.read')")
  @GetMapping(value = ["/{maintainerId}"], produces = ["application/json"])
  fun getMaintainer(@PathVariable("maintainerId") maintainerId: Long): ResponseEntity<MaintainerPojo> =
    when ( val maintainer = getMaintainerCommand.execute(maintainerId)) {
      is MaintainerPojo -> ResponseEntity.ok(maintainer)
      else -> ResponseEntity.notFound().build()
    }

  @PreAuthorize("#oauth2.hasScope('madhatter.athena.entity.create')")
  @PostMapping(value = ["/upload"], produces = ["application/json"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  fun saveMaintainers(@RequestParam("file") file: MultipartFile): ResponseEntity<Any> =
    when (val error = importMaintainerCsvCommand.execute(file)) {
      is String -> {
        System.out.println("Upload file error: $error")
        ResponseEntity(ErrorResponse(error), HttpStatus.INTERNAL_SERVER_ERROR)
      }
      else -> ResponseEntity(HttpStatus.OK)
    }

  @PreAuthorize("#oauth2.hasScope('madhatter.athena.entity.edit')")
  @PutMapping(value = ["/{maintainerId}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
  fun updateMaintainer(@PathVariable("maintainerId") maintainerId: Long, @RequestBody maintainer: MaintainerPojo): ResponseEntity<Any> {
    updateMaintainerCommand.execute(maintainer)
    return ResponseEntity(HttpStatus.OK)
  }

  @PreAuthorize("#oauth2.hasScope('madhatter.athena.entity.create')")
  @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
  fun createMaintainer(@RequestBody maintainer: NewMaintainer): ResponseEntity<Any> = try {
    createMaintainerCommand.execute(maintainer)
    ResponseEntity(HttpStatus.OK)
  } catch (ex: Exception) {
    ResponseEntity(ErrorResponse("Failed to Create Maintainer."), HttpStatus.INTERNAL_SERVER_ERROR)
  }
}
