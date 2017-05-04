import * as React from "react";
import { Navbar, Glyphicon, Button } from "react-bootstrap";
import { ArticleTable, ArticleItem } from "./ArticleTable" 
import { ArticleModal } from "./ArticleModal"

export interface AppState {
    viewArticleVisible?: boolean;
    addArticleVisible?: boolean;
    articleHeading?: string;
    articleBody?: string;

    articles: ArticleItem[];
}

export class App extends React.Component<undefined, AppState> {
    private loadArticles()
    {

    }

    private addArticle()
    {

        this.setState({addArticleVisible: false});
    }

    private showArticleDetails(articleIndex: number)
    {
        var article = this.state.articles[articleIndex];
        this.setState({
            viewArticleVisible: true,
            articleHeading: article.title,
            articleBody:    article.body
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
                <Navbar>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href="#">Fake News</a>
                        </Navbar.Brand>
                    </Navbar.Header>
                    <Navbar.Collapse>
                        <Navbar.Text>
                            <Button onClick={() => { this.showArticleAdd() }}>
                                <Glyphicon glyph="pencil" />
                            </Button>
                        </Navbar.Text>
                    </Navbar.Collapse>
                </Navbar>

                <ArticleTable articles={this.state.articles} 
                              onShowArticleDetails={this.showArticleDetails.bind(this)}/>

                <ArticleModal isVisible={this.state.viewArticleVisible} articleHeading={this.state.articleBody} 
                              articleBody={this.state.articleBody}
                              readonly
                              onAccept={() => { this.closeArticleDetails() }}
                              onCancel={() => { this.closeArticleDetails() }}
                              title="Read the article"  />

                <ArticleModal isVisible={this.state.addArticleVisible} articleHeading={this.state.articleBody} 
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