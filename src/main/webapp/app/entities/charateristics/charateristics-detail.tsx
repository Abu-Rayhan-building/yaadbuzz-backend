import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './charateristics.reducer';
import { ICharateristics } from 'app/shared/model/charateristics.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICharateristicsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CharateristicsDetail = (props: ICharateristicsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { charateristicsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="yaadmaanApp.charateristics.detail.title">Charateristics</Translate> [<b>{charateristicsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="title">
              <Translate contentKey="yaadmaanApp.charateristics.title">Title</Translate>
            </span>
          </dt>
          <dd>{charateristicsEntity.title}</dd>
          <dt>
            <Translate contentKey="yaadmaanApp.charateristics.department">Department</Translate>
          </dt>
          <dd>{charateristicsEntity.departmentId ? charateristicsEntity.departmentId : ''}</dd>
        </dl>
        <Button tag={Link} to="/charateristics" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/charateristics/${charateristicsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ charateristics }: IRootState) => ({
  charateristicsEntity: charateristics.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CharateristicsDetail);
