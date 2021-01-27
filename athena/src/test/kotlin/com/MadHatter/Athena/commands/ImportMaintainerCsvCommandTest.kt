package com.MadHatter.Athena.commands

import com.MadHatter.Athena.models.Certificate
import com.MadHatter.Athena.models.Maintainer
import com.MadHatter.Athena.models.Section
import com.MadHatter.Athena.models.Shift
import com.MadHatter.Athena.repositories.CertificateRepository
import com.MadHatter.Athena.repositories.MaintainerRepository
import com.MadHatter.Athena.repositories.SectionRepository
import com.MadHatter.Athena.repositories.ShiftRepository
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import org.junit.Assert.assertThat


@RunWith(SpringRunner::class)
@AutoConfigureStubRunner
class ImportMaintainerCsvCommandTest {

  private val headerRow = "lastName,firstName,employeeNumber,section,courseCode,assignedShift,firstPreference," +
      "secondPreference,thirdPreference,level,rank\n"

  @Mock
  lateinit var maintainerRepository: MaintainerRepository

  @Mock
  lateinit var shiftRepository: ShiftRepository

  @Mock
  lateinit var sectionRepository: SectionRepository

  @Mock
  lateinit var certificateRepository: CertificateRepository

  @Mock
  lateinit var userNameSessionCommand: UserNameSessionCommandInterface

  @Captor
  lateinit var captor: ArgumentCaptor<ArrayList<Maintainer>>

  @InjectMocks
  private var importMaintainerCsvCommand: ImportMaintainerCsvCommand = ImportMaintainerCsvCommand()

  @Before
  fun setupSections() {
    given(sectionRepository.getByName("Production"))
        .willReturn(listOf(Section("Production")))
    given(sectionRepository.getByName("Weapons"))
        .willReturn(listOf(Section("Weapons")))
    given(sectionRepository.getByName("Support"))
        .willReturn(listOf(Section("Support")))
    given(sectionRepository.getByName("APG"))
        .willReturn(listOf(Section("APG")))
    given(sectionRepository.getByName("Specialists"))
        .willReturn(listOf(Section("Specialists")))
  }

  @Before
  fun setupShifts() {
    given(shiftRepository.getByName("D"))
        .willReturn(listOf(Shift("D")))
    given(shiftRepository.getByName("S"))
        .willReturn(listOf(Shift("S")))
    given(shiftRepository.getByName("M"))
        .willReturn(listOf(Shift("M")))
  }

  @Before
  fun setupCertifications() {
    given(certificateRepository.getByName("I/E"))
        .willReturn(listOf(Certificate("I/E")))
    given(certificateRepository.getByName("TS"))
        .willReturn(listOf(Certificate("TS")))
    given(certificateRepository.getByName("HP"))
        .willReturn(listOf(Certificate("HP")))
  }

  @Test
  fun parseSingleMaintainerInfo() {
    val text: String = headerRow + "ANNESE,TIMOTHY J,5971,Production,002052,S,D,S,M,3,Sgt"
    val csvFile = MockMultipartFile("file", "filename.csv", "multipart/form-data", text.toByteArray())

    val expectedMaintainer = Maintainer("TIMOTHY J", "ANNESE", "5971",
        listOf(Certificate("I/E")),
        Shift("D"),
        Shift("S"),
        Shift("M"),
        "3",
        "Sgt",
        Section("Production"),
        Shift("S"),
        mutableListOf(),
        mutableListOf())

    importMaintainerCsvCommand.execute(csvFile)
    verify(maintainerRepository, times(1)).saveAll(captor.capture())

    val capturedMaintainers = captor.value
    assertEquals(expectedMaintainer, capturedMaintainers[0])
  }

  @Test
  fun parseSingleMaintainerManyCerts() {
    val text: String = headerRow +
        "ANNESE,TIMOTHY J,5971,Production,002260,S,D,S,M,3,Sgt\n" +
        "ANNESE,TIMOTHY J,5971,Production,002277,S,D,S,M,3,Sgt\n"
    val csvFile = MockMultipartFile("file", "filename.csv", "multipart/form-data", text.toByteArray())

    importMaintainerCsvCommand.execute(csvFile)

    verify(maintainerRepository, times(1)).saveAll(captor.capture())
    val capturedMaintainers = captor.value

    assertEquals(1, capturedMaintainers.size)
    assertEquals(listOf(Certificate("HP")), capturedMaintainers[0].certificates)
  }

  @Test
  fun parseManyMaintainersManyCerts() {
    val file = File("./src/test/kotlin/com/MadHatter/Athena/dataFiles/validMaintainerList.csv")
    val text: String = file.readText(Charsets.UTF_8)
    val csvFile = MockMultipartFile("file", "filename.csv", "multipart/form-data", text.toByteArray())

    importMaintainerCsvCommand.execute(csvFile)

    verify(maintainerRepository, times(1)).saveAll(captor.capture())
    val capturedMaintainers = captor.value

    assertEquals(4, capturedMaintainers.size)
    assertTrue(capturedMaintainers[2].certificates.containsAll(
        listOf(Certificate("HP"), Certificate("TS"), Certificate("I/E"))))
  }

  @Test
  fun addsCertsToExistingMaintainer() {
    val text: String = headerRow + "ANNESE,TIMOTHY J,5971,Production,002052,S,D,S,M,3,Sgt"
    val csvFile = MockMultipartFile("file", "filename.csv", "multipart/form-data", text.toByteArray())

    val existingMaintainer = Maintainer("TIMOTHY J", "ANNESE", "5971",
        listOf(Certificate("Bore")),
        Shift("D"),
        Shift("S"),
        Shift("M"),
        "3",
        "Sgt",
        Section("Production"),
        Shift("S"),
        mutableListOf(),
        mutableListOf(),
        1337)

    given(maintainerRepository.getByFirstNameAndLastNameAndEmployeeId("TIMOTHY J", "ANNESE", "5971"))
        .willReturn(existingMaintainer)

    importMaintainerCsvCommand.execute(csvFile)
    verify(maintainerRepository, times(1)).saveAll(captor.capture())
    val capturedMaintainers = captor.value

    assertEquals(1, capturedMaintainers.size)
    assertEquals(existingMaintainer.id, capturedMaintainers[0].id)
    assertThat(capturedMaintainers[0].certificates, containsInAnyOrder(Certificate("Bore"), Certificate("I/E")))
  }

  @Test
  fun parseInvalidMaintainerList() {
    val file = File("./src/test/kotlin/com/MadHatter/Athena/dataFiles/invalidMaintainerList.csv")
    val text: String = file.readText(Charsets.UTF_8)
    val firstFile = MockMultipartFile("file", "filename.csv", "multipart/form-data", text.toByteArray())


    val result = importMaintainerCsvCommand.execute(firstFile)

    verify(maintainerRepository, times(0)).saveAll(captor.capture())
    assertEquals("No maintainers created (Invalid Level).", result)
  }

  @Test
  fun parseEmptyMaintainerList() {
    val text = ""
    val firstFile = MockMultipartFile("file", "filename.csv", "multipart/form-data", text.toByteArray())

    val result = importMaintainerCsvCommand.execute(firstFile)

    verify(maintainerRepository, times(0)).saveAll(captor.capture())
    assertEquals("No maintainers created (File Empty).", result)
  }
}
