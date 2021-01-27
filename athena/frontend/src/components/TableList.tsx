import * as React from "react"

interface Props {
  id?: string,
  columnHeaders: string[],
  rows: JSX.Element[],
  headerClickedCallback?: (header: string) => void,
}

export default class TableList extends React.Component<Props, {}> {

  public render() {
    const id = this.props.id !== undefined ? this.props.id : ""
    return (
      <table id={id}>
        <thead>
        <tr>
          {this.props.columnHeaders.map((columnName, index) => {
            return (
              <th key={index}>{columnName}</th>
            )
          })}
        </tr>
        </thead>
        <tbody>
        {this.props.rows}
        </tbody>
      </table>
    )
  }
}
