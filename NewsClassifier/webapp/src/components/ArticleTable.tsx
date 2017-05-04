import * as React from "react";
import { Table } from "react-bootstrap";

export type ArticleRating = "agree" | "disagree" | "discuss" | "unrelated";

export interface ArticleItem {
    heading: string;
    article: string;
    origin: string;
    pubTimestamp: number;
    classificationTimestamp: number;
    rating: ArticleRating;
}

export interface ArticleTableProps {
    articles: ArticleItem[];
    onShowArticleDetails?: (index: number) => void;
}

export class ArticleTable extends React.Component<ArticleTableProps, undefined> {
    private prettyTime(epoch: number): string
    {
        return new Date(epoch).toLocaleString();
    }

    private prettyRating(rating: ArticleRating): JSX.Element {
        if(rating == "agree")
            return <span className="text-success">{rating}</span>
        else if(rating == "disagree")
            return <span className="text-warning">{rating}</span>
        else if(rating == "discuss")
            return <span className="text-info">{rating}</span>
        else
            return <span className="text-danger">{rating}</span>
    }

    private bgRating(rating: ArticleRating): string {
        if(rating == "agree")
            return "bg-success"
        else if(rating == "disagree")
            return "bg-warning"
        else if(rating == "discuss")
            return "bg-info"
        else
            return "bg-danger"
    }
    
    render() {
        let rows = this.props.articles.map((value: ArticleItem, index: number, array: ArticleItem[]) => {
            return (
                 <tr className={this.bgRating(value.rating)}>
                    <td><a href="#" onClick={() => this.props.onShowArticleDetails(index)}>{value.heading}</a></td>
                    <td>{this.prettyTime(value.classificationTimestamp)}</td>
                    <td>{this.prettyRating(value.rating)}</td>
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