import {Checkbox, Tab, Tabs} from "@material-ui/core"
import {Moment} from "moment"
import * as React from "react"
import {ChangeEvent} from "react"
import RequestHelper from "../../helpers/RequestHelper"
import Certificate from "../../models/Certificate"
import {Maintainer} from "../../models/Maintainer"
import BackButton from "../BackButton/BackButton"
import TableList from "../TableList"
import CreateAbsenceForm from "./CreateAbsenceForm"
import {MaintainerAbsenceRow} from "./MaintainerAbsenceRow"
import {RedXForm} from "./RedXForm"

interface Props {
  maintainer: Maintainer,
  reloadMaintainer: (maintainerId: number) => void
}

interface State {
  selectedTab: number
}

const certificateNames = new Map([["I/E", "Intake and Exhaust"],
  ["Run", "Engine Run"],
  ["HP", "Hot Pits"],
  ["TS", "Tow Supervisor"],
  ["Bore", "Borescope"]])

class MaintainerDetail extends React.Component<Props, State> {

  constructor(props: any) {
    super(props)
    this.state = {selectedTab: 0}
  }

  public render() {
    const {maintainer} = this.props
    const {selectedTab} = this.state
    return (
      <div className="maintainer-details">
        <BackButton />
        <h1>
          {`${maintainer.firstName} `}
          {`${maintainer.lastName} `}
          {`${maintainer.rank}`}
        </h1>
        <div className="maintainer-attributes">
          {`${maintainer.level} `}
          {`${maintainer.shift.name} `}
          {`${this.buildCertificationDisplayTexts()} `}
          {`${maintainer.firstShiftPreference.name} `}
          {`${maintainer.secondShiftPreference.name} `}
          {`${maintainer.thirdShiftPreference.name}`}
        </div>

        <RedXForm
          certs={maintainer.certificates}
          onSave={(updatedCerts) => {
            this.updateMaintainer({...maintainer, certificates: updatedCerts})
          }}/>
        <Tabs
          value={this.state.selectedTab}
          onChange={this.handleTabChange}
          className="certification_table_header maintainer-detail-group">
          <Tab
            className="schedule_tab"
            disableRipple
            label="Schedule"/>
          <Tab
            className="certifications_tab"
            disableRipple
            label="Certifications"/>
        </Tabs>

        {selectedTab === 0 &&
        <div>
          <CreateAbsenceForm createAbsence={this.createAbsence}/>
          {maintainer.absences.length !== 0 &&
          <div className="table-list">
            <h2 className="absence-list-title">Upcoming Absences</h2>
            <TableList
              id={"absence-list"}
              columnHeaders={["Beginning", "Ending", "Hours", "Location", "Reason"]}
              rows={maintainer.absences.map((absence, index) =>
                <MaintainerAbsenceRow
                  absence={absence}
                  key={index}
                  deleteAbsence={this.deleteAbsence}
                />
              )}
            />
          </div>
          }
        </div>
        }
        {selectedTab === 1 &&
        <div className="maintainer-detail-group">
          <TableList
            id="certification-list"
            columnHeaders={["Certification", "Completed"]}
            rows={Array.from(certificateNames).map(([abbreviation, certificateName], index) => {
              return (
                <tr key={index} className="certification_detail">
                  <td>{certificateName}</td>
                  <td className="checkbox_container">
                    <Checkbox
                      className="certification_checkbox"
                      checked={this.hasCertificate(abbreviation)}
                      onChange={() => this.handleCertificationChange(abbreviation)}
                      value={abbreviation}
                    />
                  </td>
                </tr>
              )
            })}
          />
        </div>
        }
      </div>
    )
  }

  private buildCertificationDisplayTexts = () => {
    return this.props.maintainer.certificates.map((certificate: Certificate) => ` ${certificate.name}`)
  }

  private hasCertificate = (abbreviation: string): boolean => (
    !!this.props.maintainer.certificates.find((cert) => cert.name === abbreviation)
  )

  private handleTabChange = (event: ChangeEvent<{}>, value: any) => {
    this.setState({selectedTab: value})
  }

  private handleCertificationChange = (abbreviation: string) => {
    const certificates = this.props.maintainer.certificates
    let updatedCertificates = certificates
    if (certificates.find((cert) => cert.name === abbreviation)) {
      updatedCertificates = certificates.filter((certificate) =>
        certificate.name !== abbreviation
      )
    } else {
      updatedCertificates.push(new Certificate(abbreviation))
    }
    this.updateMaintainer({...this.props.maintainer, certificates: updatedCertificates})
  }

  private createAbsence = async (
    startDate: Moment | null,
    endDate: Moment | null,
    reason: string | undefined,
    hours: string | undefined,
    location: string | undefined
  ): Promise<boolean> => {

    if (!startDate || !endDate || !reason) {
      return false
    }

    const jsonData: string = JSON.stringify({
      maintainerId: this.props.maintainer.id,
      startDate: startDate.format("YYYY-MM-DD"),
      endDate: endDate.format("YYYY-MM-DD"),
      reason,
      hours,
      location
    })

    let errors: boolean
    errors = await RequestHelper.postJson("/api/absences/create", jsonData)
    if (errors) {
      return false
    }

    this.props.reloadMaintainer(this.props.maintainer.id)

    return true
  }

  private deleteAbsence = async (absenceId: number): Promise<boolean> => {
    let errors: boolean
    errors = await RequestHelper.delete(`/api/absences/delete/${absenceId}`)
    if (errors) {
      return false
    }

    this.props.reloadMaintainer(this.props.maintainer.id)

    return true
  }

  private updateMaintainer = async (maintainer: Maintainer): Promise<void> => {
    let errors: boolean
    errors = await RequestHelper.put(`/api/maintainers/${maintainer.id}`, maintainer)

    if (!errors) {
      this.props.reloadMaintainer(maintainer.id)
    }
  }
}

export default MaintainerDetail
