import * as React from "react";
import { Table, Pagination, FormGroup, InputGroup, DropdownButton, MenuItem, FormControl } from "react-bootstrap";

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

export interface ArticleTableState {
    currentPage?: number;
    filterName?: string;
    filterStatus?: string;
}

export class ArticleTable extends React.Component<ArticleTableProps, ArticleTableState> {
    constructor() {
        super();
        this.state = { 
            currentPage: 1,
            filterName: "",
            filterStatus: null
         };
    }

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

    private handleSelect(pageNo: number) {
        this.setState({
            currentPage: pageNo
        });
    }

    private handleSetNameFilter(filterName: string) {
        this.setState({
            filterName: filterName
        });
    }

    private handleSetStatusFilter(filterStatus: string) {
        this.setState({
            filterStatus: filterStatus
        });
    }
    
    render() {
        let rows = this.props.articles
                    .filter((value: ArticleItem) => {
                        return !this.state.filterName || value.heading.toLowerCase().indexOf(this.state.filterName.toLowerCase()) >= 0;
                    })
                    .filter((value: ArticleItem) => {
                        return !this.state.filterStatus || value.rating == this.state.filterStatus;
                    })
                    .map((value: ArticleItem, index: number, array: ArticleItem[]) => {
            return (
                 <tr className={this.bgRating(value.rating)}>
                    <td><a href="#" onClick={() => this.props.onShowArticleDetails(index)}>{value.heading}</a></td>
                    <td>{this.prettyTime(value.classificationTimestamp)}</td>
                    <td>{this.prettyRating(value.rating)}</td>
                </tr>);
        });

        console.log(rows);

        return (
            <div className="main-container">
                <div className="text-center">
                    <Pagination
                        prev
                        next
                        first
                        last
                        ellipsis
                        boundaryLinks
                        items={Math.floor(rows.length/10)}
                        maxButtons={5}
                        activePage={this.state.currentPage}
                        onSelect={this.handleSelect.bind(this)} />
                </div>
                <FormGroup>
                    <InputGroup>
                        <FormControl type="text" value={this.state.filterName} onChange={(ev) => {
                                this.handleSetNameFilter((ev.target as any).value);
                            }}/>
                        <DropdownButton
                            componentClass={InputGroup.Button}
                            id="input-dropdown-addon"
                            title="Status">
                            <MenuItem key="0" onClick={() => this.handleSetStatusFilter(null)}><span className="text-success">no filter</span></MenuItem>
                            <MenuItem key="1" onClick={() => this.handleSetStatusFilter("agree")}><span className="text-success">agree</span></MenuItem>
                            <MenuItem key="2" onClick={() => this.handleSetStatusFilter("disagree")}><span className="text-warning">disagree</span></MenuItem>
                            <MenuItem key="3" onClick={() => this.handleSetStatusFilter("discuss")}><span className="text-info"   >discuss</span></MenuItem>
                            <MenuItem key="4" onClick={() => this.handleSetStatusFilter("unrelated")}><span className="text-danger" >unrelated</span></MenuItem>
                        </DropdownButton>
                    </InputGroup>
                </FormGroup>
                <Table striped bordered condensed hover responsive>
                    <thead>
                        <th width="70%">Title</th>
                        <th width="15%">Date</th>
                        <th width="15%">Rate</th>
                    </thead>
                    <tbody>
                        {rows.slice((this.state.currentPage-1)*10, (this.state.currentPage)*10-1)}
                    </tbody>
                </Table>
            </div>
        );
    }
}