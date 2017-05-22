import * as React from "react";
import { Navbar, Glyphicon, Nav, NavItem } from "react-bootstrap";
import { ArticleTable, ArticleItem } from "./ArticleTable" 
import { ArticleModal } from "./ArticleModal"
import * as NProgress from "nprogress"
import * as axios from "axios"

export interface AppState {
    viewArticleVisible?: boolean;
    addArticleVisible?: boolean;
    articleHeading?: string;
    articleBody?: string;
    articleOrigin?: string;
    articlePublished?: string;

    articles: ArticleItem[];
}

export class App extends React.Component<undefined, AppState> {
    constructor()
    {
        super();
        this.state = {
            articles: []
        }
    }
    
    componentDidMount()
    {
        this.loadArticles();
    }

    private loadArticles()
    {
        NProgress.start();
        axios.default.get("/api/list")
            .then((response) => {
                this.setState({articles: response.data});
                NProgress.done();
            });
    }

    private addArticle()
    {
        axios.default.post("/api/push", {
            heading: this.state.articleHeading,
            article: this.state.articleBody,
            origin: "external"
        });
        this.setState({addArticleVisible: false});
    }

    private prettyTime(epoch: number): string
    {
        return new Date(epoch).toLocaleString();
    }

    private showArticleDetails(articleIndex: number)
    {
        var article = this.state.articles[articleIndex];
        this.setState({
            viewArticleVisible: true,
            articleHeading: article.heading,
            articleBody:    article.article,
            articleOrigin:  article.origin,
            articlePublished: this.prettyTime(article.pubTimestamp)
        })
    }

    private showArticleAdd()
    {
        this.setState({
            articleHeading: "",
            articleBody: "",
            addArticleVisible: true
        })
    }

    private closeArticleDetails()
    {
        this.setState({viewArticleVisible: false});
    }

    private closeArticleAdd()
    {
        this.setState({addArticleVisible: false});
    }

    render() {
        return (
            <div>
                <Navbar inverse>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href="#">Fake News</a>
                        </Navbar.Brand>
                    </Navbar.Header>
                    <Navbar.Collapse>
                        <Nav>
                            <NavItem eventKey={1} href="#" onClick={() => { this.showArticleAdd() }}>
                                <Glyphicon glyph="pencil" />
                                Add article
                            </NavItem>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>

                <ArticleTable articles={this.state.articles} 
                              onShowArticleDetails={this.showArticleDetails.bind(this)}/>

                <ArticleModal isVisible={this.state.viewArticleVisible} 
                              articleHeading={this.state.articleHeading} 
                              articleBody={this.state.articleBody}
                              articleOrigin={this.state.articleOrigin}
                              articlePublished={this.state.articlePublished}
                              readonly
                              onAccept={() => { this.closeArticleDetails() }}
                              onCancel={() => { this.closeArticleDetails() }}
                              title="Read the article"  />

                <ArticleModal isVisible={this.state.addArticleVisible} 
                              articleHeading={this.state.articleHeading} 
                              articleBody={this.state.articleBody}
                              onAccept={() => { this.addArticle() }}
                              onCancel={() => { this.closeArticleAdd() }}
                              onChange={(e: any) => {
                                this.setState({[e.target.id]: e.target.value});
                              }}
                              title="Add an article"  />
            </div>
        )
    }
}