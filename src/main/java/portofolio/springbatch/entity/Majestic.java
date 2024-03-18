package portofolio.springbatch.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "domain_rank")
public class Majestic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "global_rank")
    private int globalRank;

    @Column(name = "tld_rank")
    private int tldRank;

    @Column(name = "domain")
    private String domain;

    @Column(name = "tld")
    private String tld;

    @Column(name = "ref_subnets")
    private int refSubNets;

    @Column(name = "ref_ips")
    private int refIPs;

    @Column(name = "idn_domain")
    private String idnDomain;

    @Column(name = "idn_tld")
    private String idnTld;

    @Column(name = "prev_global_rank")
    private int prevGlobalRank;

    @Column(name = "prev_tld_rank")
    private int prevTldRank;

    @Column(name = "prev_ref_subnets")
    private int prevRefSubNets;

    @Column(name = "prev_ref_ips")
    private int prevRefIPs;
}

