import * as React from "react"
import {Absence} from "../../models/Absence"

interface Props {
  absences: Absence[]
}

const AbsenceCell = ({absences}: Props) => (
  absences.length === 0
    ? emptyAbsenceCell()
    : busyAbsenceCell(absences)
)

const emptyAbsenceCell = () => (
  <td className={"weekly-view-absence-cell"}/>
)

const busyAbsenceCell = (absences: Absence[]) => (
  <td className={"weekly-view-absence-cell busy"}>
    {absences.length === 1
      ? singleAbsenceCell(absences[0])
      : multipleAbsenceCell(absences.length)
    }
  </td>
)

const singleAbsenceCell = (absence: Absence) => (
  <>
    <div className={"absence-reason"}>{absence.reason}</div>
    {absence.hours.length !== 0 && <div className="absence-hours">{absence.hours}</div>}
  </>
)

const multipleAbsenceCell = (absenceCount: number) => (
  <div className={"absence-reason"}>{`${absenceCount} Events`}</div>
)

export default AbsenceCell
