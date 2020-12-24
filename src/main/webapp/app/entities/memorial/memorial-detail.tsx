import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './memorial.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMemorialDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemorialDetail = (props: IMemorialDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { memorialEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memorialDetailsHeading">
          <Translate contentKey="yaadbuzzApp.memorial.detail.title">Memorial</Translate> [<strong>{memorialEntity.id}</strong>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.anonymousComment">Anonymous Comment</Translate>
          </dt>
          <dd>{memorialEntity.anonymousComment ? memorialEntity.anonymousComment.id : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.notAnonymousComment">Not Anonymous Comment</Translate>
          </dt>
          <dd>{memorialEntity.notAnonymousComment ? memorialEntity.notAnonymousComment.id : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.writer">Writer</Translate>
          </dt>
          <dd>{memorialEntity.writer ? memorialEntity.writer.id : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.recipient">Recipient</Translate>
          </dt>
          <dd>{memorialEntity.recipient ? memorialEntity.recipient.id : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.memorial.department">Department</Translate>
          </dt>
          <dd>{memorialEntity.department ? memorialEntity.department.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/memorial" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/memorial/${memorialEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ memorial }: IRootState) => ({
  memorialEntity: memorial.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemorialDetail);
