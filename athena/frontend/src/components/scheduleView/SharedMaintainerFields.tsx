import * as React from "react"
import {buildCertificateColumns} from "../../helpers/ComponentHelper"
import {Maintainer} from "../../models/Maintainer"

interface Props {
  maintainer: Maintainer,
  certificateHeaders: string[]
}

export const SharedMaintainerFields = ({maintainer, certificateHeaders}: Props) => (
  <>
    <td scope="row">
        {maintainer.lastName}, {maintainer.firstName}
    </td>
    <td>{maintainer.rank}</td>
    <td>{maintainer.shift.friendlyName()}</td>
    <td>{maintainer.level}</td>
    {buildCertificateColumns(maintainer, certificateHeaders)}
  </>
)
