import * as React from "react";
import { Table } from "react-bootstrap";

export interface ArticleItem {
    title: string;
    date: Date;
    rate: string;
}

export interface ArticleTableProps {
    data: ArticleItem[];
}

export class ArticleTable extends React.Component<ArticleTableProps, undefined> {
    render() {
        let rows = this.props.data.map((value: ArticleItem, index: number, array: ArticleItem[]) => {
            return (
                 <tr>
                    <td>{value.title}</td>
                    <td>{value.date}</td>
                    <td>{value.rate}</td>
                </tr>);
        });

        return (
            <Table striped bordered condensed hover>
                <thead>
                    <th width="70%">Title</th>
                    <th width="15%">Date</th>
                    <th width="15%">Rate</th>
                </thead>
                <tbody>
                    {rows}
                </tbody>
            </Table>
        );
    }
}