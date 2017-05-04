import * as React from "react";
import { Table } from "react-bootstrap";

export interface ArticleItem {
    title: string;
    body: string;
    date: Date;
    rate: string;
}

export interface ArticleTableProps {
    articles: ArticleItem[];
    onShowArticleDetails?: (index: number) => void;
}

export class ArticleTable extends React.Component<ArticleTableProps, undefined> {
    render() {
        let rows = this.props.articles.map((value: ArticleItem, index: number, array: ArticleItem[]) => {
            return (
                 <tr>
                    <td><a href="#" onClick={() => this.props.onShowArticleDetails(index)}>{value.title}</a></td>
                    <td>{value.date}</td>
                    <td>{value.rate}</td>
                </tr>);
        });

        return (
            <Table striped bordered condensed hover responsive>
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