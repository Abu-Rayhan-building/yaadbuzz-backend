import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserPerDepartment from './user-per-department';
import Department from './department';
import Memory from './memory';
import Comment from './comment';
import Picture from './picture';
import Topic from './topic';
import Charateristics from './charateristics';
import Memorial from './memorial';
import TopicVote from './topic-vote';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}user-per-department`} component={UserPerDepartment} />
      <ErrorBoundaryRoute path={`${match.url}department`} component={Department} />
      <ErrorBoundaryRoute path={`${match.url}memory`} component={Memory} />
      <ErrorBoundaryRoute path={`${match.url}comment`} component={Comment} />
      <ErrorBoundaryRoute path={`${match.url}picture`} component={Picture} />
      <ErrorBoundaryRoute path={`${match.url}topic`} component={Topic} />
      <ErrorBoundaryRoute path={`${match.url}charateristics`} component={Charateristics} />
      <ErrorBoundaryRoute path={`${match.url}memorial`} component={Memorial} />
      <ErrorBoundaryRoute path={`${match.url}topic-vote`} component={TopicVote} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
