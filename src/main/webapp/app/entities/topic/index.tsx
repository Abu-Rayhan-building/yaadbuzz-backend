import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Topic from './topic';
import TopicDetail from './topic-detail';
import TopicUpdate from './topic-update';
import TopicDeleteDialog from './topic-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TopicUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TopicUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TopicDetail} />
      <ErrorBoundaryRoute path={match.url} component={Topic} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TopicDeleteDialog} />
  </>
);

export default Routes;
