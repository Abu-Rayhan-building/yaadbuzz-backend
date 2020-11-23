import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TopicRating from './topic-rating';
import TopicRatingDetail from './topic-rating-detail';
import TopicRatingUpdate from './topic-rating-update';
import TopicRatingDeleteDialog from './topic-rating-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TopicRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TopicRatingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TopicRatingDetail} />
      <ErrorBoundaryRoute path={match.url} component={TopicRating} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TopicRatingDeleteDialog} />
  </>
);

export default Routes;
