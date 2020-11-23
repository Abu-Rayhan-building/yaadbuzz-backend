import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './topic-rating.reducer';
import { ITopicRating } from 'app/shared/model/topic-rating.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITopicRatingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const TopicRating = (props: ITopicRatingProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { topicRatingList, match, loading } = props;
  return (
    <div>
      <h2 id="topic-rating-heading">
        <Translate contentKey="yaadmaanApp.topicRating.home.title">Topic Ratings</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="yaadmaanApp.topicRating.home.createLabel">Create new Topic Rating</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {topicRatingList && topicRatingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.topicRating.repetitions">Repetitions</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.topicRating.rating">Rating</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.topicRating.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {topicRatingList.map((topicRating, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${topicRating.id}`} color="link" size="sm">
                      {topicRating.id}
                    </Button>
                  </td>
                  <td>{topicRating.repetitions}</td>
                  <td>{topicRating.ratingId ? <Link to={`topic/${topicRating.ratingId}`}>{topicRating.ratingId}</Link> : ''}</td>
                  <td>{topicRating.userId ? <Link to={`user-per-department/${topicRating.userId}`}>{topicRating.userId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${topicRating.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${topicRating.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${topicRating.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="yaadmaanApp.topicRating.home.notFound">No Topic Ratings found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ topicRating }: IRootState) => ({
  topicRatingList: topicRating.entities,
  loading: topicRating.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TopicRating);
