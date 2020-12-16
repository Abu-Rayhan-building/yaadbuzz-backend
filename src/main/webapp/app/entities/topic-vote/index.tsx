import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TopicVote from './topic-vote';
import TopicVoteDetail from './topic-vote-detail';
import TopicVoteUpdate from './topic-vote-update';
import TopicVoteDeleteDialog from './topic-vote-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TopicVoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TopicVoteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TopicVoteDetail} />
      <ErrorBoundaryRoute path={match.url} component={TopicVote} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TopicVoteDeleteDialog} />
  </>
);

export default Routes;
