import moment from "moment"
import * as React from "react"
import {Absence} from "../../models/Absence"

interface Props {
  absence: Absence
  deleteAbsence: (arg0: number) => Promise<boolean>
}

export const MaintainerAbsenceRow = ({absence, deleteAbsence}: Props) => (
  <tr className="absence">
    <td>{moment(absence.startDate).format("MM/DD/YYYY")}</td>
    <td>{moment(absence.endDate).format("MM/DD/YYYY")}</td>
    <td>{absence.hours ? absence.hours : "Entire Shift"}</td>
    <td>{absence.location}</td>
    <td>{absence.reason}</td>
    <td>
      <button onClick={() => deleteAbsence(absence.id)} className="delete-absence button-basic">Delete Absence</button>
    </td>
  </tr>
)
